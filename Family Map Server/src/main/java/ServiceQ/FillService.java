package ServiceQ;

import DataAccess.*;
import Encoding.DataLoader;
import Encoding.EventLoader;
import Handler.ClientError;
import Handler.ServerError;
import Model.Event;
import Model.Person;
import Model.User;
import RequestQ.FillRequest;
import Response.FillResponse;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

/**
 * This class will interact with the database to see if relevant data is able to be inserted into the database
 * for a specified user
 */
public class FillService
{
    private Database db;
    //private static Connection conn = null;

    private Vector<Person> thisGeneration;
    private Vector<Person> nextGeneration;
    private Vector<Person> resultantPersons;

    private Vector<Event> resultantEvents; //FIXME

    private Event[] eventIdeas;
    private String[] femaleNames;
    private String[] maleNames;
    private String[] lastNames;

    private User thisUser;

    private PersonDao personDao;
    private EventDao eventDao;

    private int yearLowerBound = 1995;
    private int yearUpperBound = 2000;

    int numEvents = 0;

    /**
     * Constructor
     */
    public FillService()
    {
        numEvents = 0;
        yearUpperBound = 2000;
        yearLowerBound = 1995;
    }

    /**
     *
     * @return a FillResponse object signifying whether the fill request was able to be established or not
     */
    public FillResponse fillFuntimes(FillRequest fr) throws ServerError, ClientError, DataAccessError {

        try {

            this.db = new Database();


            halfEvents(); //Get all the event ideas from the json file.
            maleNames();
            femaleNames();
            snames();

            this.db.openConnection();

            if (fr.getGenerations() <= 0) {
                db.closeConnection(false);
                db = null;
                throw new ClientError("Error: Provide valid number.");
            }
            ///////////////////////////////////////////////////////////////

            personDao = new PersonDao(this.db.getConnection());

            UserDao userDao = new UserDao(this.db.getConnection());

            personDao.deleteAssociatedPersons(fr.getUserName());

            eventDao = new EventDao(db.getConnection());
            eventDao.deleteAssociatedEvents(fr.getUserName());

            ///////////////////////////////////////////////////////////////
            //This will be to set the first person, which will have the same info, largely, as the user
            User thisUser = userDao.find(fr.getUserName());
            if (thisUser == null) {
                db.closeConnection(false);
                db = null;
                throw new ClientError("Error: Try a different user." );
            }
            String originalID = thisUser.getPersonId();
            //String thisID = randomString();

            Person relevantPerson = new Person(originalID,thisUser.getUsername(),thisUser.getFirstName(),thisUser.getLastName(),
                    thisUser.getGender(),null,null,null);
            userDao.setPersonId(thisUser.getUsername(),originalID); //This sets the new Person ID for this User

            //////////////////////////////////////////////////////////////
            getAll(thisUser,fr.getGenerations(),relevantPerson);
            /////////////////////////////////////////////////////////////
            for (int i=0; i < resultantPersons.size(); i++) {
                personDao.insert(resultantPersons.elementAt(i));
            }

            db.closeConnection(true);
            return new FillResponse("Successfully added " + resultantPersons.size() + " persons and " +
            numEvents + " events into the database" , true);

        } catch (DataAccessError  e) {
            db.closeConnection(false);
            db = null;
            throw new ServerError("Error: " + e.getMessage());
        }
        catch (ClientError e) {
           throw new ClientError("Error: " + e.getMessage());
        }
        catch (Exception e) {
            db.closeConnection(false);
            db = null;
            throw new ServerError("Error: " + e.getMessage());
        }
    }

    public void getAll(User aUser, int generations, Person rootPerson) throws ServerError, DataAccessError {

       thisGeneration = new Vector<Person>();
       resultantPersons = new Vector<Person>();
       resultantEvents = new Vector<Event>(); //FIXME
       thisUser = aUser;
       thisGeneration.add(rootPerson);

        for (int i=0; i <= generations; i++) {

            nextGeneration = new Vector<Person>();

            for (int j=0; j < thisGeneration.size(); j++) {
                try {
                    getParents(j, i, generations);
                } catch (DataAccessError e) {
                    db.closeConnection(false);
                    db = null;
                    throw new ServerError("Error: " + e.getMessage());
                }
            }
            thisGeneration = nextGeneration;
            yearLowerBound-=26;
            yearUpperBound-=26;
        }

    }

    public void getParents (int index, int currentGeneration, int numGenerations) throws DataAccessError {

        Person thisPerson = thisGeneration.elementAt(index);

        String fatherID = randomString();
        String motherID = randomString();

        if (thisPerson.getPersonId() == null) {
            String thisPersonID = randomString();
        }

        Person randomFather = getRandomFather(fatherID,thisPerson.getLastName(),motherID);
        Person randomMother = getRandomMother(motherID,fatherID);

        if (currentGeneration != numGenerations) {
            thisPerson.setFatherID(fatherID);
            thisPerson.setMotherID(motherID);
        }
        resultantPersons.add(thisPerson);
        nextGeneration.add(randomFather);
        nextGeneration.add(randomMother);

        //Get an event for "thisPerson", randomly
        if (currentGeneration == 0) {
            attachEvents(thisPerson,false);
        } else {
            attachEvents(thisPerson,true);
        }
    }

    public Person getRandomMother(String motherID, String husbandID) {

        //Person result = new Person(motherID,thisUserName,)
        String firstName = femaleNames[(getRandomIntegerBetweenRange((double)0,(double)femaleNames.length-1))];
        String lastName = lastNames[(getRandomIntegerBetweenRange((double)0,(double)lastNames.length-1))];
        return new Person(motherID,thisUser.getUsername(),firstName,lastName,"f",null,null,husbandID);
    }

    public Person getRandomFather(String fatherID, String lastName, String wifeID) {
        String firstName = maleNames[(getRandomIntegerBetweenRange((double)0,(double)maleNames.length-1))];
        return new Person(fatherID,thisUser.getUsername(),firstName,lastName,"m",null,null,wifeID);
    }

    public void attachEvents (Person thisPerson, boolean needsDeath) throws DataAccessError {

            Event randomBirth = eventIdeas[(getRandomIntegerBetweenRange((double) 0, (double) eventIdeas.length-1))];
            randomBirth.setEventType("Birth");
            int whatRandomYear = getRandomIntegerBetweenRange((double) yearLowerBound, (double) yearUpperBound);
            randomBirth.setYear(whatRandomYear);

            String eventID = randomString();

            randomBirth.setEventID(eventID);
            randomBirth.setAssociatedUsername(thisUser.getUsername());
            randomBirth.setPersonID(thisPerson.getPersonId());

            eventDao.insert(randomBirth);

            //resultantEvents.add(randomBirth); //FIXME
            numEvents++;

        //Country, City, Latitude, Longitude, Type
        //Needed:

        if (needsDeath) {
            Event randomDeath = eventIdeas[(getRandomIntegerBetweenRange((double) 0, (double) eventIdeas.length-1))];
            randomDeath.setEventType("Death");
            whatRandomYear = getRandomIntegerBetweenRange((double)yearLowerBound+60,(double)yearUpperBound+60);
            randomDeath.setYear(whatRandomYear);

            eventID = randomString();

            randomDeath.setEventID(eventID);
            randomDeath.setAssociatedUsername(thisUser.getUsername());
            randomDeath.setPersonID(thisPerson.getPersonId());
            eventDao.insert(randomDeath);
            //resultantEvents.add(randomDeath);
            numEvents++;

            ////////////Now on to the marriages/////////////////////////
            String spouseID = thisPerson.getSpouseID();
            eventID = randomString();

            Event isThereMarriage = eventDao.findAssociatedMarriage(spouseID);

            if (isThereMarriage == null) { //Construct new marriage year
                Event randomMarriage = eventIdeas[(getRandomIntegerBetweenRange((double) 0, (double) eventIdeas.length-1))];
                whatRandomYear = getRandomIntegerBetweenRange((double)yearLowerBound+18,(double)yearUpperBound+18);
                randomMarriage.setYear(whatRandomYear);
                randomMarriage.setEventType("Marriage");
                randomMarriage.setAssociatedUsername(thisUser.getUsername());
                randomMarriage.setEventID(eventID);
                randomMarriage.setPersonID(thisPerson.getPersonId());
                eventDao.insert(randomMarriage);
                //resultantEvents.add(randomMarriage);
                numEvents++;
            }
            //Use current marriage year and location
            else {
                isThereMarriage.setPersonID(thisPerson.getPersonId());
                isThereMarriage.setEventID(eventID);
                eventDao.insert(isThereMarriage);
                //resultantEvents.add(isThereMarriage);
                numEvents++;
            }
        }
    }

    public void halfEvents() throws ServerError {
        Gson gson = new Gson();
        try {
            EventLoader eventLoader = gson.fromJson(new FileReader("json/locations.json"), EventLoader.class);
            //eventIdeas = events;
            eventIdeas = eventLoader.getData();

        } catch (IOException e) {
            try {
                if (db != null) {
                    db.closeConnection(false);
                    db = null;
                }
            } catch (DataAccessError dataAccessError) {
                dataAccessError.printStackTrace();
                throw new ServerError("Error: " + e.getMessage() + " and " + dataAccessError.getMessage());
            }
            throw new ServerError("Error: " + e.getMessage());
        }
    }

    public void maleNames() throws ServerError {
        Gson gson = new Gson();
        try {
           DataLoader dataLoader = gson.fromJson(new FileReader("json/mnames.json"), DataLoader.class);
           maleNames = dataLoader.getData();

        } catch (IOException e) {
            try {
                if (db != null) {
                    db.closeConnection(false);
                    db = null;
                }
            } catch (DataAccessError dataAccessError) {
                dataAccessError.printStackTrace();
            }
            throw new ServerError("Error: " + e.getMessage());
        }
    }

    public void femaleNames() throws ServerError {
        Gson gson = new Gson();
        try {
            DataLoader dataLoader = gson.fromJson(new FileReader("json/fnames.json"), DataLoader.class);
            femaleNames = dataLoader.getData();

        } catch (IOException e) {
            try {
                if (db != null) {
                    db.closeConnection(false);
                    db = null;
                }
            } catch (DataAccessError dataAccessError) {
                dataAccessError.printStackTrace();
            }
            throw new ServerError("Error: " + e.getMessage());
        }
    }

    public void snames() throws ServerError {
        Gson gson = new Gson();
        try {
            DataLoader dataLoader = gson.fromJson(new FileReader("json/snames.json"), DataLoader.class);
            lastNames = dataLoader.getData();

        } catch (IOException e) {
            try {
                if (db != null) {
                    db.closeConnection(false);
                    db = null;
                }
            } catch (DataAccessError dataAccessError) {
                dataAccessError.printStackTrace();
            }
            throw new ServerError("Error: " + e.getMessage());
        }
    }


    /*From https://dzone.com/articles/random-number-generation-in-java */
    public static int getRandomIntegerBetweenRange(double min, double max) {
        double x = (int)(Math.random()*((max-min)+1))+min;
        return (int)x;
    }


    public String randomString() {
         return UUID.randomUUID().toString();
    }
}
