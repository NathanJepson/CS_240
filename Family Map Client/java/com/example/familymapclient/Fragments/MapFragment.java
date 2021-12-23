package com.example.familymapclient.Fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymapclient.Model.StaticEventSorter;
import com.google.android.gms.maps.CameraUpdate;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.Model.PersonCluster;
import com.example.familymapclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback,
        GoogleMap.OnMarkerClickListener, EventFragment.OnFragmentInteractionListener {

    public static final String ARG_TITLE = "title";
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388e3c;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;

    private static final int LIGHT_BLUE = 0xadd8e6;
    private static final int LIGHT_PINK = 0xFFB6C1;
    private static final int PURPLE = 0x00800080;
    private static final int BLUE = Color.argb(155,3,9,255);
    private static final int PINK = 0x00FFC0CB;
    private static final int WATERMELON = 0x00fe7f9c;

    private Event mostRecentEvent = null;
    List<Polyline> polylines = new ArrayList<Polyline>();

    private boolean firstFragment = true;

    private static final int LINE_THICKNESS = 13;
    private static final int FAMILY_LINE_THICKNESS = 26;

    public SupportMapFragment mapFragment;

    private GoogleMap mMap;

    private String mParam1;
    private String mParam2;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private final boolean isEventActivity;
    private String EventID = null;

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public MapFragment(boolean thisIsEventActivity){
        isEventActivity = thisIsEventActivity;
    }


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment(false);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If no settings have been configured for available sides of the family...
        if (DataCache.instance().getAvailableEvents() == null) {
            //Then simply set all events as on the selected side /sides of the family
            DataCache.instance().DetermineAvailableEvents();
        }

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (firstFragment) {
            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fm.beginTransaction();

            EventFragment ef = new EventFragment("NULL_EVENT"); //This string will be used to not
            //transfer the user to the Person Activity when the Event fragment is clicked

            Bundle args = new Bundle();
            ft.add(R.id.EventFrame, ef)
                    .commit();
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

       mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       mapFragment.getMapAsync(this);

        //Iconify.with(new FontAwesomeModule());
        //ImageView imageView = new ImageView();
        //imageView.setImageDrawable(settingsIcon);
        //toolbar.addView(ImageView);
        //toolbar.setLogo
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        DrawMarkers();

        //Activity tmp = this.getActivity();
        if (isEventActivity) {
            Event thisEvent = DataCache.instance().relevantEvent(this.EventID);
            LatLng latLng = new LatLng(thisEvent.getLatitude(),thisEvent.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,0));//Recenter the camera on the correct marker
            GetEventInfo(this.EventID);

            if (DataCache.instance().isSpouseLines()) {
                //You can only draw spouse lines if both genders are available
                if (DataCache.instance().isFemaleEvents() && DataCache.instance().isMaleEvents()) {
                    DrawSpouseLines(this.EventID);
                }
            }
            if (DataCache.instance().isLifeStoryLines()) {
                DrawLifeStoryLines(this.EventID);
            }

            if (DataCache.instance().isFamilyTreeLines()) {
                DrawFamilyTreeLines(this.EventID);
            }

            DrawLines(); //We now know enough about the previous marker to redraw its lines
        }
    }


    //This function becomes really important to use when settings are changed in the settings activity
    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment != null) {
            mapFragment.onResume();

            ClearMap(); //Removes all markers and polylines
            FlushLines(); //Clears polylines list
            DrawMarkers(); //This function accounts for which gender and side
            //of the family is selected; don't worry
        }

        //If an event has been selected since the activity started, then we can the lines and event window
        //for the last event--unless the event is not longer available because of filters
        if (mostRecentEvent != null) {

            boolean isMale = true;
            boolean isAvailable = true;

            //What gender is the event attached to?
            if (DataCache.instance().getIndexedPersons().get(mostRecentEvent.getPersonID()).getThePerson().
                    getGender().equals("f")) {
                isMale = false;
            }

            //Is the event associated with currently-available sides of the family?
            if (!DataCache.instance().getAvailableEvents().contains(mostRecentEvent.getEventID())) {
                isAvailable = false;
            }

            //Only draw the most recent event if the corresponding gender setting is switched on in settings, as well
              //as if it is on the available sides of the family
            if ( ((DataCache.instance().isMaleEvents() && isMale) || (DataCache.instance().isFemaleEvents() &&
                    !isMale)) && isAvailable) {

                LatLng latLng = new LatLng(mostRecentEvent.getLatitude(),mostRecentEvent.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,0));//Recenter the camera on the correct marker
                GetEventInfo(mostRecentEvent.getEventID()); //Draw the event window at the bottom (and set the
                //fragment's event ID)

                if (DataCache.instance().isSpouseLines()) {
                    //You can only draw spouse lines if both genders are available
                    if (DataCache.instance().isFemaleEvents() && DataCache.instance().isMaleEvents()) {
                        DrawSpouseLines(mostRecentEvent.getEventID());
                    }
                }
                if (DataCache.instance().isLifeStoryLines()) {
                    DrawLifeStoryLines(mostRecentEvent.getEventID());
                }

                if (DataCache.instance().isFamilyTreeLines()) {
                    DrawFamilyTreeLines(mostRecentEvent.getEventID());
                }

                DrawLines(); //We now know enough about the previous marker to redraw its lines

            } else {
                //Since the previous selected marker is filtered out, clear the window on the bottom
                FragmentManager fm = getChildFragmentManager();
                EventFragment currentEventFrag = (EventFragment) fm.findFragmentById(R.id.EventFrame);
                currentEventFrag.setEVENT_ID("NULL_EVENT");

                View view = this.getView();
                ImageView genderIconPlace = (ImageView)view.findViewById(R.id.gender);
                TextView tmp1 = view.findViewById(R.id.mapTextView);
                TextView tmp2 = view.findViewById(R.id.mapTextView2);

                genderIconPlace.setImageResource(android.R.color.transparent);
                tmp1.setText("Click on a marker to interact with it");
                tmp2.setText("");
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        DeleteLines(); //Delete all lines associated with the previously clicked marker

        mostRecentEvent = DataCache.instance().relevantEvent((String)marker.getTag());
        marker.showInfoWindow();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),0));

        String eventID = (String)marker.getTag();
        GetEventInfo((String)marker.getTag());

        if (DataCache.instance().isSpouseLines()) {
            //Check if both genders are available to draw spouse lines between, then draw those lines
            if (DataCache.instance().isFemaleEvents() && DataCache.instance().isMaleEvents()) {
                DrawSpouseLines(eventID);
            }
        }
        if (DataCache.instance().isLifeStoryLines()) {
            DrawLifeStoryLines(eventID);
        }
        if (DataCache.instance().isFamilyTreeLines()) {
            DrawFamilyTreeLines(eventID);
        }

        return true;
    }


    //This function mainly sets the window and draws the lower end of the map fragment with all event
    //information
    private void GetEventInfo(String eventID) {
        PersonCluster personCluster  = DataCache.instance().getIndexedPersons().
                get(DataCache.instance().getIndexedEvents().get(eventID));
        Event relevantEvent = DataCache.instance().relevantEvent(eventID);

        //LatLng latLng = new LatLng(relevantEvent.getLatitude(),relevantEvent.getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        Person relevantPerson = personCluster.getThePerson();
        View view = this.getView();

        //EventFragment eventFragment = new EventFragment(eventID);
        FragmentManager fm = getChildFragmentManager();
        EventFragment currentEventFrag = (EventFragment) fm.findFragmentById(R.id.EventFrame);
        currentEventFrag.setEVENT_ID(eventID); //This gives the event ID to the Event Fragment, so it can
        //know to send it to the Person Activity when it is clicked

        firstFragment = false;

        if (relevantPerson.getGender().equals("m")) {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).color(BLUE).sizeDp(34);
            ImageView genderIconPlace = (ImageView)view.findViewById(R.id.gender);
            genderIconPlace.setImageDrawable(genderIcon);
        } else if (relevantPerson.getGender().equals("f")) {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).color(WATERMELON).sizeDp(34);
            ImageView genderIconPlace = (ImageView)view.findViewById(R.id.gender);
            genderIconPlace.setImageDrawable(genderIcon);
        }

        TextView tmp1 = view.findViewById(R.id.mapTextView);
        String tmpStr = relevantPerson.getFirstName() + " " + relevantPerson.getLastName();
        tmp1.setText(tmpStr);

        TextView tmp2 = view.findViewById(R.id.mapTextView2);
        String tmpStr2 = relevantEvent.getEventType() + ": " + relevantEvent.getCity() + ", " + "\n" +
                relevantEvent.getCountry() + " (" + relevantEvent.getYear() + ")";
        tmp2.setText(tmpStr2);

        // ImageView imageView = (ImageView) mapFragment.getView().findViewById(R.id.image);
    }



    private void DrawLifeStoryLines(String eventID) {
        Event thisPersonEvent = DataCache.instance().relevantEvent(eventID);

        Event[] personsEvents = DataCache.instance().getIndexedPersons()
                .get(thisPersonEvent.getPersonID()).getPersonEvents();

        //Event birthEvent = DataCache.instance().getIndexedPersons().get(thisPersonEvent.getPersonID()).getBirth();
        //Event deathEvent = null;
        //Event marriageEvent = null;

       List<Event> resultantEvents = new LinkedList<>();

        for (int i=0; i < personsEvents.length; i++) {
            resultantEvents.add(personsEvents[i]);
        }

        resultantEvents = StaticEventSorter.sortedEvents(resultantEvents);

        for (int i=0; i < resultantEvents.size()-1; i++) {
            LatLng src = new LatLng(resultantEvents.get(i).getLatitude(),
                    resultantEvents.get(i).getLongitude());
            LatLng dst = new LatLng(resultantEvents.get(i+1).getLatitude(),
                    resultantEvents.get(i+1).getLongitude());

                polylines.add(mMap.addPolyline(new PolylineOptions()
                        .clickable(false)
                        .add(src,dst)
                        .color(COLOR_ORANGE_ARGB)
                ));
        }

    }

    private void DrawSpouseLines(String eventID) {
       // Marker marker = mMap.findMarker

        Event thisPersonEvent = DataCache.instance().relevantEvent(eventID);
        Event spouseEvent = null;

        try {
            if (thisPersonEvent != null) {

                String spouseID = DataCache.instance().getIndexedPersons().get(thisPersonEvent.getPersonID()).getThePerson().getSpouseID();

                if (spouseID != null) { //If this person actually has a spouse
                    if (DataCache.instance().getIndexedPersons().get(spouseID) != null) {
                        //Person spouse = DataCache.instance().getIndexedPersons().get(spouseID).getThePerson();
                        PersonCluster theSpouse = DataCache.instance().getIndexedPersons().get(spouseID);
                        Event[] spouseEvents = theSpouse.getPersonEvents();
                        List<Event> toSort = new LinkedList<>();
                        for (int i=0; i < spouseEvents.length; i++) {
                            toSort.add(spouseEvents[i]);
                        }
                        toSort = StaticEventSorter.sortedEvents(toSort);
                        spouseEvent = toSort.get(0);
                    }
                }

            } else {
                throw new Exception("Event not indexed correctly");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (spouseEvent == null) {
            return;
        }

        polylines.add(mMap.addPolyline(new PolylineOptions()
            .clickable(false)
            .add (
                    new LatLng(thisPersonEvent.getLatitude(),thisPersonEvent.getLongitude()),
                    new LatLng(spouseEvent.getLatitude(),spouseEvent.getLongitude())
            )
                .color(COLOR_GREEN_ARGB)
        ));
        //polyline.setColor(COLOR_PURPLE_ARGB);
        //spouseLine.setVisible(true);
        //spouseLine.setWidth(LINE_THICKNESS);
    }

    private void DrawFamilyTreeLines(String eventID) {
        DrawFamilyTreeLines_helper(DataCache.instance().relevantEvent(eventID),FAMILY_LINE_THICKNESS);
    }

    private void DrawFamilyTreeLines_helper(Event givenEvent, float currentThickness) {
        PersonCluster rootPerson = DataCache.instance().getIndexedPersons().get(givenEvent.getPersonID());

        if (rootPerson.getThePerson().getFatherID() != null) {
            Event fatherEvent = null;
            String fatherID = rootPerson.getThePerson().getFatherID();
            PersonCluster father = DataCache.instance().getIndexedPersons().get(fatherID);

            Event[] fatherEvents = father.getPersonEvents();
            List<Event> toSortEvents = new LinkedList<>();
            for (int i=0; i < fatherEvents.length; i++) {
                toSortEvents.add(fatherEvents[i]);
            }
            toSortEvents = StaticEventSorter.sortedEvents(toSortEvents);
            fatherEvent = toSortEvents.get(0); //Get the earliest event, using the sorted list

            if (fatherEvent != null) {
                //If male events are turned on, and this is on the side/sides of the family that are turned on
                if (DataCache.instance().getAvailableEvents().contains(fatherEvent.getEventID()) &&
                        DataCache.instance().isMaleEvents()) {
                    polylines.add(mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .add(
                                    new LatLng(givenEvent.getLatitude(), givenEvent.getLongitude()),
                                    new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude())
                            )
                            .width(currentThickness)
                            .color(BLUE)
                    ));
                    float tmp = (float) (currentThickness * 0.55);
                    DrawFamilyTreeLines_helper(fatherEvent,tmp);
                }
            }
        }

        if (rootPerson.getThePerson().getMotherID() != null) {
            Event motherEvent = null;
            String motherID = rootPerson.getThePerson().getMotherID();
            PersonCluster mother = DataCache.instance().getIndexedPersons().get(motherID);

            Event[] motherEvents = mother.getPersonEvents();
            List<Event> toSortEvents = new LinkedList<>();
            for (int i=0; i < motherEvents.length; i++) {
                toSortEvents.add(motherEvents[i]);
            }
            toSortEvents = StaticEventSorter.sortedEvents(toSortEvents);
            motherEvent = toSortEvents.get(0); //Get the earliest event, using the sorted list

            if (motherEvent != null) {
                //If the mother is on the correct side / sides of the family, and female events are turned on
                if (DataCache.instance().getAvailableEvents().contains(motherEvent.getEventID()) &&
                        DataCache.instance().isFemaleEvents()) {
                    polylines.add(mMap.addPolyline(new PolylineOptions()
                            .clickable(false)
                            .add(
                                    new LatLng(givenEvent.getLatitude(), givenEvent.getLongitude()),
                                    new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude())
                            )
                            .width(currentThickness)
                            .color(BLUE)
                    ));
                    float tmp = (float) (currentThickness * 0.55);
                    DrawFamilyTreeLines_helper(motherEvent,tmp);
                }
            }
        }
    }

    private void ClearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    private void FlushLines() {
        if (polylines != null) {
            polylines.clear();
        }
    }

    private void DeleteLines() {
        if (polylines != null) {
            for (Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();
        }
    }



    private void DrawLines() {
        for(Polyline line : polylines)
        {
            line.setVisible(true);
        }
    }


    private void DrawMarkers() {
        if (mMap == null) {
            return;
        }

        if (!DataCache.instance().isMaleEvents() && !DataCache.instance().isFemaleEvents()) {
            return; //Draw no markers if Male and Female events are turned off; this saves the program
            //from traversing everything below
        }

        for (int i=0; i < DataCache.instance().getEvents().length; i++) {
            Event thisEvent = DataCache.instance().getEvents()[i];

            if (thisEvent.getCountry().toLowerCase().equals("japan")) {
                System.out.println("Oh baby");
            }
            Person relevantPerson = DataCache.instance().getIndexedPersons().
                    get(thisEvent.getPersonID()).getThePerson();

            LatLng thisLocation = new LatLng(thisEvent.getLatitude(),thisEvent.getLongitude());
            Marker thisMarker;

            boolean isMale = true;
            if (relevantPerson.getGender().equals("f")) {
                isMale = false;
            }

            //Only draw the marker if the corresponding gender setting is switched on in settings
            if ( (DataCache.instance().isMaleEvents() && isMale) || (DataCache.instance().isFemaleEvents() &&
                    !isMale)) {
                if (DataCache.instance().getAvailableEvents() != null) {
                    if (!DataCache.instance().getAvailableEvents().contains(thisEvent.getEventID())) {
                        continue; //If the event wasn't marked as available after setting for Mother and Father
                        //events, then simply continue the loop
                    }
                }
                float color = -1;

                    if (thisEvent.getEventType().toLowerCase().equals("marriage")) {
                        color = BitmapDescriptorFactory.HUE_AZURE;
                    } else if (thisEvent.getEventType().toLowerCase().equals("birth")) {
                        color = BitmapDescriptorFactory.HUE_MAGENTA;
                    } else if (thisEvent.getEventType().toLowerCase().equals("death")) {
                       color = BitmapDescriptorFactory.HUE_RED;
                    } else if (thisEvent.getEventType().toLowerCase().equals("completed asteroids")) {
                        color = BitmapDescriptorFactory.HUE_CYAN;
                    } else if (thisEvent.getEventType().toLowerCase().equals("graduated from byu")) {
                        color = BitmapDescriptorFactory.HUE_BLUE;
                    } else if (thisEvent.getEventType().toLowerCase().equals("learned to surf")) {
                        color = BitmapDescriptorFactory.HUE_GREEN;
                    } else if (thisEvent.getEventType().toLowerCase().equals("ate brazilian barbecue")) {
                        color = BitmapDescriptorFactory.HUE_YELLOW;
                    } else if (thisEvent.getEventType().toLowerCase().equals("caught a frog")) {
                        color = BitmapDescriptorFactory.HUE_VIOLET;
                    } else if (thisEvent.getEventType().toLowerCase().equals("learned java")) {
                        color = BitmapDescriptorFactory.HUE_ROSE;
                    } else if (thisEvent.getEventType().toLowerCase().equals("did a backflip")) {
                        color = BitmapDescriptorFactory.HUE_ORANGE;
                    }

                thisMarker = mMap.addMarker(new MarkerOptions()
                        .position(thisLocation)
                        .snippet(thisEvent.getCity() + ", " + thisEvent.getCountry())
                        .icon(BitmapDescriptorFactory.defaultMarker(color)));
                thisMarker.setTag(thisEvent.getEventID());
            }

        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //This function deals for when an event window is clicked on.
    @Override
    public void onFragmentInteraction(String msg) {
        if (msg.contains("Event")) {
            mListener.onFragmentInteraction(msg);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String msg);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction("Random button");
        }
    }

    @Override
    public void onStart() {

        super.onStart();
        if (mapFragment != null) {
            mapFragment.onStart();
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mapFragment != null) {
            mapFragment.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapFragment != null) {
            mapFragment.onDestroy();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapFragment != null) {
            mapFragment.onStop();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapFragment != null) {
            mapFragment.onLowMemory();
        }
    }

}
