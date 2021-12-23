package com.example.familymapclient.Model;

public class PersonCluster {

    private Person thePerson;
    //private Event birth;
    //private Event marriage;
   // private Event death;
    private Event[] personEvents;

    public Event[] getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Event[] personEvents) {
        this.personEvents = personEvents;
    }

    public Person getThePerson() {
        return thePerson;
    }

    public void setThePerson(Person thePerson) {
        this.thePerson = thePerson;
    }

}
