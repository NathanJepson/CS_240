package com.example.familymapclient.Model;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//This class will be used to grab all available events; used for Settings and Person activities
//NOTE: This class assumes that certain items in the DataCache are not null (which are usually set when the map...
// ...fragment is first started
public class DataGenerator {

    private DataGenerator() {}

    private static DataGenerator _instance = null;

    private static List<Event> availableEvents = null;

    public static DataGenerator instance() {
        if (_instance == null) {
            _instance = new DataGenerator();
        }
        return _instance;
    }

    //This will be used to set what available events and persons can be used
    public void initializeData() {
        availableEvents = new LinkedList<>();

        for (Map.Entry<String,PersonCluster> entry : DataCache.instance().getIndexedPersons().entrySet()) {

            String personID = entry.getKey();
            PersonCluster thisPerson = DataCache.instance().getIndexedPersons().get(personID);

            //If the relevant gender for this person is allowed by settings
            if ( (thisPerson.getThePerson().getGender().equals("f") && DataCache.instance().isFemaleEvents())
                || (thisPerson.getThePerson().getGender().equals("m") && DataCache.instance().isMaleEvents())) {
                    //availablePersons.add(thisPerson.getThePerson());
                Event[] thisEvents = thisPerson.getPersonEvents();
                for (int i=0; i < thisEvents.length; i++) {

                    //If the event is on the right side / sides of the family
                    if (DataCache.instance().getAvailableEvents().contains(thisEvents[i].getEventID())) {
                        availableEvents.add(thisEvents[i]);
                    }
                }
                   //availableEvents.addAll(Arrays.asList(thisEvents));
            }
        }
    }

    public List<Event> getAvailableEvents() {
        if (availableEvents == null) {
            initializeData();
        }
        return availableEvents;
    }

    public List<Person> getAvailablePersons() {
        Person[] theOldWay = DataCache.instance().getPersons();
        List<Person> result = new LinkedList<Person>();
        for (int i = 0; i< DataCache.instance().getPersons().length; i++) {
            result.add(theOldWay[i]);
        }
        return result;
    }

    public void clearDataGenerator() {
        if (availableEvents != null) {
            availableEvents.clear();
        }
        availableEvents = null;
        _instance = null;
    }

}
