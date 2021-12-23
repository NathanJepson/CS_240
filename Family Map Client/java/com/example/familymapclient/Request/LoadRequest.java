package com.example.familymapclient.Request;


import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.Model.User;

/**
 * This is a request to load a person's user, person, and event data into the database
 * after clearing the database
 */

public class LoadRequest
{
    private User[] users;
    private Person[] persons;
    private Event[] events;

    /**
     *
     * @param users An array of users to be created in the database
     * @param persons An array of persons to be created in the database
     * @param events An array of events to be created in the database
     */
    public LoadRequest(User[] users, Person[] persons, Event[] events)
    {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers()
    {
        return users;
    }

    public void setUsers(User[] users)
    {
        this.users = users;
    }

    public Person[] getPersons()
    {
        return persons;
    }

    public void setPersons(Person[] persons)
    {
        this.persons = persons;
    }

    public Event[] getEvents()
    {
        return events;
    }

    public void setEvents(Event[] events)
    {
        this.events = events;
    }
}
