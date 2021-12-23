package ServiceQ;

import DataAccess.*;
import Handler.ServerError;
import Model.Event;
import Model.Person;
import Model.User;
import RequestQ.LoadRequest;
import Response.LoadResponse;

import java.sql.Connection;
import java.sql.SQLException;

public class LoadService {

    private Database db;

    public LoadService () { }

    public LoadResponse getLoadResponse(LoadRequest lq) throws ServerError {

        try {
            db = new Database();
            db.openConnection();
            db.clearTables();
            UserDao userDao = new UserDao(db.getConnection());
            PersonDao personDao = new PersonDao(db.getConnection());
            EventDao eventDao = new EventDao(db.getConnection());

            for (int i=0; i < lq.getUsers().length; i++) {
                User thisUser = new User(lq.getUsers()[i].getUsername(),lq.getUsers()[i].getPassword(),
                        lq.getUsers()[i].getEmail(),lq.getUsers()[i].getFirstName(),lq.getUsers()[i].getLastName(),
                        lq.getUsers()[i].getGender(),lq.getUsers()[i].getPersonId() );
                userDao.insert(thisUser);
            }
            for (int i=0; i < lq.getPersons().length; i++) {
                Person thisPerson = new Person(lq.getPersons()[i].getPersonId(),lq.getPersons()[i].getAssociatedUsername(),
                        lq.getPersons()[i].getFirstName(),lq.getPersons()[i].getLastName(),lq.getPersons()[i].getGender(),
                        lq.getPersons()[i].getFatherID(),lq.getPersons()[i].getMotherID(),lq.getPersons()[i].getSpouseID());
                personDao.insert(thisPerson);
            }
            for (int i=0; i < lq.getEvents().length; i++) {
                Event thisEvent = new Event(lq.getEvents()[i].getEventID(),lq.getEvents()[i].getAssociatedUsername(),
                        lq.getEvents()[i].getPersonID(),lq.getEvents()[i].getLatitude(),lq.getEvents()[i].getLongitude(),
                        lq.getEvents()[i].getCountry(),lq.getEvents()[i].getCity(),lq.getEvents()[i].getEventType(),
                        lq.getEvents()[i].getYear());
                eventDao.insert(thisEvent);
            }

            db.closeConnection(true);
            db = null;

            return new LoadResponse("Successfully added " + lq.getUsers().length + " users, "
            + lq.getPersons().length + " persons, and " + lq.getEvents().length + " events to the database.",
                    true);

        } catch (DataAccessError e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
                db = null;
            } catch (DataAccessError dataAccessError) {
                dataAccessError.printStackTrace();
                throw new ServerError("Error: Unable to close database (500).");
            }
            throw new ServerError("Error with the database: 500");
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }
}
