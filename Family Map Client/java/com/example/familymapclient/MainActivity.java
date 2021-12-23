package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.familymapclient.Activity.PersonActivity;
import com.example.familymapclient.Activity.SearchActivity;
import com.example.familymapclient.Activity.SettingsActivity;
import com.example.familymapclient.Fragments.EventFragment;
import com.example.familymapclient.Fragments.LoginFragment;
import com.example.familymapclient.Fragments.MapFragment;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.Model.PersonCluster;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MapFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener, EventFragment.OnFragmentInteractionListener {

    Menu menuTracker = null;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*
        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment)fm.findFragmentById(R.id.fragmentContainer);
        if (loginFragment == null) {

            loginFragment = new LoginFragment();
            Bundle args = new Bundle();
            args.putString(LoginFragment.ARG_TITLE,"Something");
            loginFragment.setArguments(args);
            fm.beginTransaction()
                    .add(R.id.fragmentContainer,loginFragment)
                    .commit();
        }

        Iconify.with(new FontAwesomeModule());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mymenu, menu);
        menuTracker = menu;
        return super.onCreateOptionsMenu(menu);
    }


    public void switchFragments() {

        MenuItem searchItem = menuTracker.findItem(R.id.search_icon);
        MenuItem settingsItem = menuTracker.findItem(R.id.settings_icon);

        Drawable gearIcon = new IconDrawable(this, FontAwesomeIcons.fa_gear).color(COLOR_WHITE_ARGB).sizeDp(28);
        Drawable searchIcon = new IconDrawable(this, FontAwesomeIcons.fa_search).color(COLOR_WHITE_ARGB).sizeDp(28);

        searchItem.setIcon(searchIcon);
        searchItem.setVisible(true);

        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                startSearchActivity();
                return false;
            }
        });

        settingsItem.setIcon(gearIcon);
        settingsItem.setVisible(true);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                startSettingsActivity();
                return false;
            }
        });

        FragmentManager fm = this.getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment)fm.findFragmentById(R.id.fragmentContainer);

        MapFragment mapFragment = new MapFragment(false);
        Bundle args = new Bundle();
        args.putString(MapFragment.ARG_TITLE,"Something");
        mapFragment.setArguments(args);

        if (loginFragment != null) {
            fm.beginTransaction()
                    .remove(loginFragment)
                    .add(R.id.fragmentContainer, mapFragment)
                    .commit();
        }
    }


    @Override
    public void onFragmentInteraction(String msg) {

        try {
            if (DataCache.instance().getPersons() != null && DataCache.instance().getEvents() != null) {
                //System.out.println("We did it, boys!");

                //This for loop is going to index all of the data for faster access later by other fragments / activities
                if (DataCache.instance().getIndexedPersons() == null || DataCache.instance().getIndexedEvents() == null) {
                   indexDataCache();
                }

                if (msg.equals("Map")) { //The main activity then knows to switch to the map fragment based on
                                            //message from login activity
                    switchFragments();
                } else if (msg.contains("Event")) {
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

    //This sequences all the data to allow for faster lookup in later parts of the app
    public void indexDataCache() {
        DataCache.instance().indexDataCache();
    }


    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void startSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void startPersonActivity(String eventID) {

        Bundle extras = new Bundle();
        extras.putString("EVENT_ID",eventID);
        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
