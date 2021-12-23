package com.example.familymapclient.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.familymapclient.Activity.PersonActivity;
import com.example.familymapclient.MainActivity;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.DataGenerator;
import com.example.familymapclient.R;


//This class is an object to write the rows in the settings activity
////////////SETTINGS ACTIVITY ITEMS//////////////////////////////////////////////
public class ListItem extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ListItem() {
        // Required empty public constructor
    }

    public static ListItem newInstance(String param1, String param2) {
        ListItem fragment = new ListItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_item, container, false);

        TextView tv1 = v.findViewById(R.id.FirstText);
        tv1.setText(mParam1);
        TextView tv2 = v.findViewById(R.id.SecondText);
        tv2.setText(mParam2);

        Switch switch1 = (Switch) v.findViewById(R.id.switch1);

        if (mParam1.equals("Life Story Lines")) {
            if (DataCache.instance().isLifeStoryLines()) {
                switch1.setChecked(true);
            }
        } else if (mParam1.equals("Family Tree Lines")) {
            if (DataCache.instance().isFamilyTreeLines()) {
                switch1.setChecked(true);
            }
        } else if (mParam1.equals("Spouse Lines")) {
            if (DataCache.instance().isSpouseLines()) {
                switch1.setChecked(true);
            }

        } else if (mParam1.equals("Father's Side")) {
            if (DataCache.instance().isFathersSide()) {
                switch1.setChecked(true);
            }

        } else if (mParam1.equals("Mother's Side")) {
            if (DataCache.instance().isMothersSide()) {
                switch1.setChecked(true);
            }

        } else if (mParam1.equals("Male Events")) {
            if (DataCache.instance().isMaleEvents()) {
                switch1.setChecked(true);
            }

        } else if (mParam1.equals("Female Events")) {
            if (DataCache.instance().isFemaleEvents()) {
                switch1.setChecked(true);
            }

        } else if (mParam1.equals("Logout")) {
            switch1.setVisibility(v.GONE);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    DataCache.instance().clearCache();
                    DataGenerator.instance().clearDataGenerator();
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               changeValue(isChecked);
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String msg);
    }

    private void changeValue(boolean checked) {
        if (mParam1.equals("Life Story Lines")) {
            DataCache.instance().setLifeStoryLines(checked);
        } else if (mParam1.equals("Family Tree Lines")) {
           DataCache.instance().setFamilyTreeLines(checked);
        } else if (mParam1.equals("Spouse Lines")) {
            DataCache.instance().setSpouseLines(checked);
        } else if (mParam1.equals("Father's Side")) {
            DataCache.instance().setFathersSide(checked);
            DataCache.instance().DetermineAvailableEvents();
        } else if (mParam1.equals("Mother's Side")) {
            DataCache.instance().setMothersSide(checked);
            DataCache.instance().DetermineAvailableEvents();
        } else if (mParam1.equals("Male Events")) {
           DataCache.instance().setMaleEvents(checked);
        } else if (mParam1.equals("Female Events")) {
           DataCache.instance().setFemaleEvents(checked);
        }
    }
}
