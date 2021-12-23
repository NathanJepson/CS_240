package com.example.familymapclient.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.Activity.EventActivity;
import com.example.familymapclient.Activity.PersonActivity;
import com.example.familymapclient.Fragments.SearchViewHolder;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.R;
import com.google.android.gms.maps.model.LatLng;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;

public class PersonAdapter extends BaseExpandableListAdapter {

    private final List<Event> events;
    private final List<Person> persons;

    private static final int EVENTS_GROUP_POSITION = 0;
    private static final int PERSONS_GROUP_POSTITION = 1;

    private static final int WATERMELON = 0x00fe7f9c;
    private static final int BLUE = Color.argb(155,3,9,255);
    private static final int ICON_SIZE = 9;


    public PersonAdapter(List<Event> theseEvents, List<Person> thesePersons) {
        this.events = theseEvents;
        this.persons = thesePersons;
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       switch(groupPosition) {
           case(EVENTS_GROUP_POSITION ):
               return events.size();
           case(PERSONS_GROUP_POSTITION):
               return persons.size();
           default:
               throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
       }
    }

    @Override
    public Object getGroup(int groupPosition) {
        switch(groupPosition) {
            case EVENTS_GROUP_POSITION:
                return ("Life Events");
            case PERSONS_GROUP_POSTITION:
                return ("Family");
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case EVENTS_GROUP_POSITION:
                return events.get(childPosition);
            case PERSONS_GROUP_POSTITION:
                return persons.get(childPosition);
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = li.inflate(R.layout.expandable_list_header,parent,false);
        }

        TextView titleView = convertView.findViewById(R.id.expandableListGroupTitle);

        switch(groupPosition) {
            case EVENTS_GROUP_POSITION:
                titleView.setText("Life Events");
                break;
            case PERSONS_GROUP_POSTITION:
                titleView.setText("Family");
                break;
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
       View itemView;
       LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch(groupPosition) {
           case EVENTS_GROUP_POSITION:
               itemView = li.inflate(R.layout.expandable_list_element_event,parent,false);
               getEventView(itemView,childPosition);
               break;
            case PERSONS_GROUP_POSTITION:
                itemView = li.inflate(R.layout.expandable_list_element_person,parent,false);
                getPersonView(itemView,childPosition);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
        }
        return itemView;
    }

    private void getEventView(View eventItemView, final int childPosition) {
        final Event thisEvent = events.get(childPosition);
        TextView textView = eventItemView.findViewById(R.id.textview1_expandableList_event);
        textView.setText(thisEvent.getEventType() + ": " + thisEvent.getCity() + ", " +
                thisEvent.getCountry() + " (" + thisEvent.getYear() + ")");
        TextView textView2 = eventItemView.findViewById(R.id.textview2_expandableList_event);
        Person thePerson = DataCache.instance().getIndexedPersons().get(thisEvent.getPersonID()).getThePerson();
        textView2.setText(thePerson.getFirstName() + " " + thePerson.getLastName());

        ImageView theImageView = eventItemView.findViewById(R.id.pin_expandableList_event);
        Drawable mapPin =  new IconDrawable(eventItemView.getContext(), FontAwesomeIcons.fa_map_marker).sizeDp(ICON_SIZE);
        theImageView.setImageDrawable(mapPin);

        eventItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("EVENT_ID",thisEvent.getEventID());
                Intent intent = new Intent(v.getContext(), EventActivity.class);

                intent.putExtras(extras);
                v.getContext().startActivity(intent);

            }
        });

    }

    private void getPersonView(View personItemView, final int childPosition) {
        final Person thisPerson = persons.get(childPosition);
        TextView textView = personItemView.findViewById(R.id.textview1_expandableList);
        textView.setText(thisPerson.getFirstName() + " " + thisPerson.getLastName());
        TextView textView2 = personItemView.findViewById(R.id.textview2_expandableList);
        textView2.setText(thisPerson.getRelationshipType());

        ImageView theImageView = personItemView.findViewById(R.id.pin_expandableList_person);
        Drawable personPin;
        if (thisPerson.getGender().equals("m")) {
           personPin =  new IconDrawable(personItemView.getContext(), FontAwesomeIcons.fa_male)
                   .color(BLUE).sizeDp(ICON_SIZE);
        } else {
            personPin =  new IconDrawable(personItemView.getContext(), FontAwesomeIcons.fa_female)
                    .color(WATERMELON).sizeDp(ICON_SIZE);
        }

        theImageView.setImageDrawable(personPin);
        personItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("PERSON_ID",thisPerson.getPersonId());
                Intent intent = new Intent(v.getContext(), PersonActivity.class);

                intent.putExtras(extras);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
