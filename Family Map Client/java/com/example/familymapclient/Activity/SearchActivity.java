package com.example.familymapclient.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.example.familymapclient.Adapter.SearchAdapter;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.DataGenerator;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.R;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchBar;
    private RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //DataGenerator.instance().initializeData();


        searchBar = findViewById(R.id.editText_search);

        recyclerView = findViewById(R.id.RecyclerView);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Required empty method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Required empty method
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (canGrabResult()) {
                    if (recyclerView != null) {
                        String thisText = searchBar.getText().toString();
                        DataGenerator.instance().initializeData();
                        List<Person> thePersons = refinedPersons(DataGenerator.instance().getAvailablePersons(), thisText);
                        List<Event> theEvents = refinedEvents(DataGenerator.instance().getAvailableEvents(), thisText);

                        SearchAdapter adapter = new SearchAdapter(thePersons, theEvents);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    recyclerView.setAdapter(null);
                }
            }
        };
        searchBar.addTextChangedListener(tw);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }


    private boolean canGrabResult() {
        return (!searchBar.getText().toString().equals(""));
    }

    private List<Person> refinedPersons(List<Person> thesePersons, String thisText) {
        List<Person> result = new LinkedList<Person>();
        String thisText2 = thisText.toLowerCase();

        for (int i=0; i < thesePersons.size(); i++ ) {
            Person thisPerson = thesePersons.get(i);
            if (thisPerson.getFirstName().toLowerCase().contains(thisText2)
            || thisPerson.getLastName().toLowerCase().contains(thisText2)) {
                result.add(thisPerson);
            }
        }
        return result;
    }

    private List<Event> refinedEvents(List<Event> theseEvents, String thisText) {
        List<Event> result = new LinkedList<Event>();
        String thisText2 = thisText.toLowerCase();

        for (int i=0; i < theseEvents.size(); i++) {
            Event thisEvent = theseEvents.get(i);
            String thisYear = Integer.toString(thisEvent.getYear());
            if (thisEvent.getEventType().toLowerCase().contains(thisText2) ||
                    thisYear.contains(thisText2) ||
                    thisEvent.getCountry().toLowerCase().contains(thisText2) ||
                    thisEvent.getCity().toLowerCase().contains(thisText2)) {
                result.add(thisEvent);
            }
        }
        return result;
    }
}
