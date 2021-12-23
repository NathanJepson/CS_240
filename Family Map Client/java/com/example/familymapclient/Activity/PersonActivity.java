package com.example.familymapclient.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.example.familymapclient.Adapter.PersonAdapter;
import com.example.familymapclient.Fragments.PersonListItem;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.DataGenerator;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.Model.PersonCluster;
import com.example.familymapclient.Model.StaticEventSorter;
import com.example.familymapclient.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PersonActivity extends AppCompatActivity {

    private static String eventID = null;
    private static String personID = null;
    private static PersonCluster thisPersonCluster = null;
    private static Person person = null;

    //private static Map<Person,String> relationships = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("EVENT_ID")) {
                eventID = extras.getString("EVENT_ID");
                personID = DataCache.instance().getIndexedEvents().get(eventID);
            } else {
                personID = extras.getString("PERSON_ID");
            }
        }

        assert(personID != null); //FIXME

        thisPersonCluster = DataCache.instance().getIndexedPersons().get(personID);
        person = thisPersonCluster.getThePerson();

        FragmentManager fm = this.getSupportFragmentManager();

        PersonListItem personListItem1 = (PersonListItem) fm.findFragmentById(R.id.frameLayout1_person);
        PersonListItem personListItem2 = (PersonListItem) fm.findFragmentById(R.id.frameLayout2_person);
        PersonListItem personListItem3 = (PersonListItem) fm.findFragmentById(R.id.frameLayout3_person);

        if (personListItem1 == null || personListItem2 == null || personListItem3 == null) {
            Bundle args = new Bundle();
            personListItem1 = new PersonListItem();

            args.putString(PersonListItem.ARG_PARAM1,person.getFirstName());
            args.putString(PersonListItem.ARG_PARAM2,"First Name");
            personListItem1.setArguments(args);

            args = new Bundle();
            personListItem2 = new PersonListItem();
            args.putString(PersonListItem.ARG_PARAM1,person.getLastName());
            args.putString(PersonListItem.ARG_PARAM2,"Last Name");
            personListItem2.setArguments(args);

            args = new Bundle();
            personListItem3 = new PersonListItem();
            String tmp;
            if (person.getGender().equals("m")) {
                tmp = "Male";
            } else {
                tmp = "Female";
            }
            args.putString(PersonListItem.ARG_PARAM1,tmp);
            args.putString(PersonListItem.ARG_PARAM2,"Gender");
            personListItem3.setArguments(args);

            fm.beginTransaction()
                    .add(R.id.frameLayout1_person,personListItem1)
                    .add(R.id.frameLayout2_person,personListItem2)
                    .add(R.id.frameLayout3_person,personListItem3)
                    .commit();
        }

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        DataGenerator.instance().initializeData();
        List<Event> relevantEvents = getEvents(DataGenerator.instance().getAvailableEvents());
        relevantEvents = StaticEventSorter.sortedEvents(relevantEvents);

        List<Person> relevantPersons = getPersons(DataGenerator.instance().getAvailablePersons());

        expandableListView.setAdapter(new PersonAdapter(relevantEvents,relevantPersons));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //System.out.println(eventID); //FIXME!!!!!!!!!!!
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private List<Person> getPersons (List<Person> thesePersons) {
        List<Person> result = new LinkedList<>();

        for (int i=0; i < thesePersons.size(); i++ ) {
            Person thisPerson = thesePersons.get(i);
            //Person is a child
            if (thisPerson.getMotherID() != null) {
                if (thisPerson.getMotherID().equals(person.getPersonId())) {
                    thisPerson.setRelationshipType("Child");
                    result.add(thisPerson);
                }
            }
            if (thisPerson.getFatherID() != null) {
                if (thisPerson.getFatherID().equals(person.getPersonId())) {
                   thisPerson.setRelationshipType("Child");
                    result.add(thisPerson);
                }
            }
            if (thisPerson.getSpouseID() != null) {
                if (thisPerson.getSpouseID().equals(person.getPersonId())) {
                    thisPerson.setRelationshipType("Spouse");
                    result.add(thisPerson);
                }
            }
            if (person.getFatherID() != null) {
                if (person.getFatherID().equals(thisPerson.getPersonId())) {
                    thisPerson.setRelationshipType("Father");
                    result.add(thisPerson);
                }
            }
            if (person.getMotherID() != null) {
                if (person.getMotherID().equals(thisPerson.getPersonId())) {
                    thisPerson.setRelationshipType("Mother");
                    result.add(thisPerson);
                }
            }
        }
        return result;
    }

    private List<Event> getEvents(List<Event> theseEvents) {
        List<Event> result = new LinkedList<Event>();
        for (int i=0; i < theseEvents.size(); i++) {
            if (theseEvents.get(i).getPersonID().equals(person.getPersonId()))  {
                result.add(theseEvents.get(i));
            }
        }
        return result;
    }

}
