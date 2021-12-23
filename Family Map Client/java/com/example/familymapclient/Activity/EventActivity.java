package com.example.familymapclient.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.familymapclient.Fragments.EventFragment;
import com.example.familymapclient.Fragments.LoginFragment;
import com.example.familymapclient.Fragments.MapFragment;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.R;

public class EventActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,
        EventFragment.OnFragmentInteractionListener {

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("EVENT_ID")) {
                eventID = extras.getString("EVENT_ID");
            }
        }

        FragmentManager fm = this.getSupportFragmentManager();
        MapFragment mapFragment = new MapFragment(true);
        mapFragment.setEventID(eventID);
        Bundle args = new Bundle();
        args.putString(MapFragment.ARG_TITLE,"Something");
        mapFragment.setArguments(args);

        fm.beginTransaction()
                .add(R.id.fragmentContainer_eventActivity,mapFragment)
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(String msg) {

        try {
            if (DataCache.instance().getPersons() != null && DataCache.instance().getEvents() != null) {
                //System.out.println("We did it, boys!");

              if (msg.contains("Event")) {
                    StringBuilder sb = new StringBuilder(msg);
                    sb.delete(0,6);
                    startPersonActivity(sb.toString());
                }

            } else {
                throw new Exception("Persons and events of this user are null.");
            }

        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void startPersonActivity(String eventID) {


        Bundle extras = new Bundle();
        extras.putString("EVENT_ID",eventID);

        Intent intent = new Intent(this, PersonActivity.class);

        intent.putExtras(extras);
        startActivity(intent);
    }

}
