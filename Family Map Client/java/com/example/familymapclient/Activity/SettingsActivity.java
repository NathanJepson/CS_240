package com.example.familymapclient.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.familymapclient.Fragments.ListItem;
import com.example.familymapclient.Fragments.MapFragment;
import com.example.familymapclient.R;

public class SettingsActivity extends AppCompatActivity implements ListItem.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        FragmentManager fm = this.getSupportFragmentManager();
        ListItem lifeStoryLinesFragment = (ListItem) fm.findFragmentById(R.id.LifeStoryLines);
        ListItem familyTreeLinesFragment = (ListItem) fm.findFragmentById(R.id.FamilyTreeLines);
        ListItem spouseLinesFragment = (ListItem) fm.findFragmentById(R.id.SpouseLines);
        ListItem fathersSideFragment = (ListItem) fm.findFragmentById(R.id.FathersSide);
        ListItem mothersSideFragment = (ListItem) fm.findFragmentById(R.id.MothersSide);
        ListItem maleEventsFragment = (ListItem) fm.findFragmentById(R.id.MaleEvents);
        ListItem femaleEventsFragment = (ListItem) fm.findFragmentById(R.id.FemaleEvents);
        ListItem logoutFragment = (ListItem) fm.findFragmentById(R.id.Logout);

        if (lifeStoryLinesFragment == null || familyTreeLinesFragment == null
        || spouseLinesFragment == null || fathersSideFragment == null || mothersSideFragment == null
            || maleEventsFragment == null || femaleEventsFragment == null || logoutFragment == null) {

            Bundle args = new Bundle();
        lifeStoryLinesFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Life Story Lines");
        args.putString(ListItem.ARG_PARAM2,"Show Life Story Lines");
        lifeStoryLinesFragment.setArguments(args);

            args = new Bundle();
        familyTreeLinesFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Family Tree Lines");
        args.putString(ListItem.ARG_PARAM2,"Show Family Tree Lines");
        familyTreeLinesFragment.setArguments(args);

            args = new Bundle();
        spouseLinesFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Spouse Lines");
        args.putString(ListItem.ARG_PARAM2,"Show Spouse Lines");
        spouseLinesFragment.setArguments(args);


            args = new Bundle();
        fathersSideFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Father's Side");
        args.putString(ListItem.ARG_PARAM2,"Filter by Father's Side of the Family");
        fathersSideFragment.setArguments(args);

            args = new Bundle();
        mothersSideFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Mother's Side");
        args.putString(ListItem.ARG_PARAM2,"Filter by Mother's Side of the Family");
        mothersSideFragment.setArguments(args);

            args = new Bundle();
        maleEventsFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Male Events");
        args.putString(ListItem.ARG_PARAM2,"Filter Based on Gender");
        maleEventsFragment.setArguments(args);

            args = new Bundle();
        femaleEventsFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Female Events");
        args.putString(ListItem.ARG_PARAM2,"Filter Based on Gender");
        femaleEventsFragment.setArguments(args);

            args = new Bundle();
        logoutFragment = new ListItem();
        args.putString(ListItem.ARG_PARAM1,"Logout");
        args.putString(ListItem.ARG_PARAM2,"Return to Login Screen");
        logoutFragment.setArguments(args);

            fm.beginTransaction()
                    .add(R.id.LifeStoryLines,lifeStoryLinesFragment)
                    .add(R.id.FamilyTreeLines,familyTreeLinesFragment)
                    .add(R.id.SpouseLines,spouseLinesFragment)
                    .add(R.id.FathersSide,fathersSideFragment)
                    .add(R.id.MothersSide,mothersSideFragment)
                    .add(R.id.MaleEvents,maleEventsFragment)
                    .add(R.id.FemaleEvents,femaleEventsFragment)
                    .add(R.id.Logout,logoutFragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(String msg) {

    }
}
