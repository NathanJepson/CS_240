package com.example.familymapclient.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataCache {


    private DataCache() {}

    private static DataCache _instance = null;

    public static DataCache instance() {

        if (_instance == null) {
            _instance = new DataCache();
        }
        return _instance;
    }

    Set<String> familyEvents = new HashSet<String>(); //This makes sure that other people not in the...
    ///...family aren't filtered out

    private static Person[] persons;
    private static Event[] events;

    private static Map<String, PersonCluster> indexedPersons;
    private static Map<String, String> indexedEvents; //This simply associates any Event ID with a personID, for fast lookup
    private static Set<String> availableEvents; //This shows which events are available for mapping
    private static String rootPerson;

    public String getRootPerson() {
        return rootPerson;
    }

    public void setRootPerson(String rootPerson) {
        DataCache.rootPerson = rootPerson;
    }

    private static boolean LifeStoryLines = true;
    private static boolean FamilyTreeLines = true;
    private static boolean SpouseLines = true;
    private static boolean FathersSide = true;
    private static boolean MothersSide = true;
    private static boolean MaleEvents = true;
    private static boolean FemaleEvents = true;

    public  boolean isLifeStoryLines() {
        return LifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        LifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return FamilyTreeLines;
    }

    public  void setFamilyTreeLines(boolean familyTreeLines) {
        FamilyTreeLines = familyTreeLines;
    }

    public  boolean isSpouseLines() {
        return SpouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        SpouseLines = spouseLines;
    }

    public  boolean isFathersSide() {
        return FathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        FathersSide = fathersSide;
    }

    public boolean isMothersSide() {
        return MothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        MothersSide = mothersSide;
    }

    public boolean isMaleEvents() {
        return MaleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        MaleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return FemaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        FemaleEvents = femaleEvents;
    }

    public Map<String, PersonCluster> getIndexedPersons() {
        return indexedPersons;
    }

    public void setIndexedPersons(Map<String, PersonCluster> indexedPersons) {
        DataCache.indexedPersons = indexedPersons;
    }

    public  Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        DataCache.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        DataCache.events = events;
    }

    public Map<String, String> getIndexedEvents() {
        return indexedEvents;
    }

    public void setIndexedEvents(Map<String, String> indexedEvents) {
        DataCache.indexedEvents = indexedEvents;
    }

    public Event relevantEvent(String eventID) {
        //PersonCluster = this.getIndexedPersons().get()
        String personID = this.getIndexedEvents().get(eventID);

        PersonCluster personCluster = this.getIndexedPersons().get(personID);
        if (personCluster != null) {
            Event[] personsEvents = personCluster.getPersonEvents();
            for (int i = 0; i < personsEvents.length; i++) {
                if (personsEvents[i].getEventID().equals(eventID)) {
                    return personsEvents[i];
                }
            }
        }
        return null;
    }

    //This sequences all the data to allow for faster lookup in later parts of the app
    public void indexDataCache() {

        HashMap<String, PersonCluster> result = new HashMap<>();
        HashMap<String,String> result2 = new HashMap<>();

        for (int i=0; i < DataCache.instance().getPersons().length; i++) {

            Person thisPerson = DataCache.instance().getPersons()[i];
            PersonCluster personCluster = new PersonCluster();
            int iterator = 0;

            //Used to determine the size of the array to hold that person's events
            for (int j=0; j < DataCache.instance().getEvents().length; j++) {
                if (DataCache.instance().getEvents()[j].getPersonID().equals(
                        DataCache.instance().getPersons()[i].getPersonId()
                )) {
                    iterator++;
                }
            }

            Event[] relevantEvents = new Event[iterator];
            int index = 0;
            for (int j=0; j < DataCache.instance().getEvents().length; j++) {

                if (DataCache.instance().getEvents()[j].getPersonID().equals(
                        DataCache.instance().getPersons()[i].getPersonId()
                )) {
                    Event thisEvent = DataCache.instance().getEvents()[j];

                    result2.put(thisEvent.getEventID(),thisPerson.getPersonId());

                    relevantEvents[index] = thisEvent;
                    index++;
                }

            }

            personCluster.setPersonEvents(relevantEvents);
            personCluster.setThePerson(thisPerson);
            result.put(thisPerson.getPersonId(),personCluster);
        }
        DataCache.instance().setIndexedPersons(result);
        DataCache.instance().setIndexedEvents(result2);
    }


    public Set<String> getAvailableEvents() {
        return availableEvents;
    }

    public void setAvailableEvents(Set<String> availableEvents) {
        DataCache.availableEvents = availableEvents;
    }

    //This will mostly be used to determine what events can be used (determined by the Father's and Mother's
    //side of the family)
    public void DetermineAvailableEvents() {
        availableEvents = new HashSet<String>();
        familyEvents = new HashSet<>();

        PersonCluster rootPerson = DataCache.instance().getIndexedPersons().get(DataCache.instance().getRootPerson());
        PersonCluster mother = null;
        PersonCluster father = null;

        if (rootPerson.getThePerson().getMotherID() != null) {
            mother = DataCache.instance().getIndexedPersons().get(rootPerson.getThePerson().getMotherID());
        }

        if (rootPerson.getThePerson().getFatherID() != null) {
            father = DataCache.instance().getIndexedPersons().get(rootPerson.getThePerson().getFatherID());
        }

        List<Event> rootsEvents = personsEvents(rootPerson.getThePerson().getPersonId());
        if ((DataCache.instance().isMaleEvents() && rootPerson.getThePerson().getGender().equals("m"))
                || (DataCache.instance().isFemaleEvents() && rootPerson.getThePerson().getGender().equals("f"))) {
            for (int i=0; i < rootsEvents.size(); i++) {
                availableEvents.add(rootsEvents.get(i).getEventID());
                familyEvents.add(rootsEvents.get(i).getEventID());
            }
        }
        boolean isAllowed = false;

        if (DataCache.instance().isMothersSide()) {
           isAllowed = true;
        }
        if (mother != null) {
            DetermineAvailableEvents_helper(mother,isAllowed);
        }

        isAllowed = false;

        if (DataCache.instance().isFathersSide()) {
           isAllowed = true;
        }
        if (father != null) {
            DetermineAvailableEvents_helper(father,isAllowed);
        }

        for (int i=0; i < getEvents().length; i++) {
            Event thisEvent = getEvents()[i];
            if (!DataCache.instance().familyEvents.contains(thisEvent.getEventID())) {
                availableEvents.add(thisEvent.getEventID());
            }
        }
    }

    private void DetermineAvailableEvents_helper(PersonCluster thisPerson, boolean allowed) {
        List<Event> thisPersonsEvents = personsEvents(thisPerson.getThePerson().getPersonId());

        for (int i=0; i < thisPersonsEvents.size(); i++) {
            if (allowed) {
                availableEvents.add(thisPersonsEvents.get(i).getEventID());
            }
            familyEvents.add(thisPersonsEvents.get(i).getEventID());
        }

        if (thisPerson.getThePerson().getMotherID() != null) {
            DetermineAvailableEvents_helper(DataCache.instance().getIndexedPersons().
                    get(thisPerson.getThePerson().getMotherID()),allowed);
        }
        if (thisPerson.getThePerson().getFatherID() != null) {
            DetermineAvailableEvents_helper(DataCache.instance().getIndexedPersons().
                    get(thisPerson.getThePerson().getFatherID()),allowed);
        }
    }


    private List<Event> personsEvents(String personID) {
        List<Event> result = new LinkedList<>();
        for (int i=0; i < events.length; i++) {
            Event thisEvent = events[i];
            if (thisEvent.getPersonID().equals(personID)) {
                result.add(thisEvent);
            }
        }
        return result;
    }

    public void clearCache() {
        availableEvents = null;
        rootPerson = null;
        indexedEvents = null;
        indexedPersons = null;
        persons = null;
        events = null;
        LifeStoryLines = true;
        FamilyTreeLines = true;
        SpouseLines = true;
        FathersSide = true;
        MothersSide = true;
        MaleEvents = true;
        FemaleEvents = true;
        _instance = null;
    }


}
