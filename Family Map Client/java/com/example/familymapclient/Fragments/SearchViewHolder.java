package com.example.familymapclient.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymapclient.Activity.EventActivity;
import com.example.familymapclient.Activity.PersonActivity;
import com.example.familymapclient.Model.DataCache;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final int WATERMELON = 0x00fe7f9c;
    private static final int BLUE = Color.argb(155,3,9,255);
    private static final int AZURE = 0xFFF0FFFF;
    private static final int MAGENTA = Color.argb(255,255,0,255);
    private static final int RED = Color.argb(255,255,0,0);


    private static final int PERSON_SEARCH_ITEM = 0;
    private static final int EVENT_SEARCH_ITEM = 1;
    private static final int ICON_SIZE = 11;

    private final int viewType;

    private View itemView;

    private final ImageView icon;
    private final TextView name;
    private final TextView type_location;
    private Event event;
    private Person person;
    private String id;

    public SearchViewHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        this.itemView = itemView;
        this.viewType = viewType;

        itemView.setOnClickListener(this);

            //Is a person
        if (viewType == PERSON_SEARCH_ITEM) {
            name = itemView.findViewById(R.id.personTextView1);
            icon = itemView.findViewById(R.id.personPin);
            type_location = null;

            //Is an event
        } else {
            name = itemView.findViewById(R.id.eventTextView2);
            type_location = itemView.findViewById(R.id.eventTextView1);
            icon = itemView.findViewById(R.id.eventPin);
        }
    }

    public void bind (Event thisEvent) {
        this.event = thisEvent;
        Person relevantPerson = DataCache.instance().getIndexedPersons().get(event.getPersonID()).getThePerson();
        name.setText(relevantPerson.getFirstName() + " " + relevantPerson.getLastName());
        type_location.setText(event.getEventType() + ": " + thisEvent.getCity() + ", " +
                thisEvent.getCountry() + " (" + thisEvent.getYear() + ")");

        id = event.getEventID();

        Drawable eventIcon;
        if (thisEvent.getEventType().equals("Marriage")) {
            eventIcon = new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_map_marker)
                    .color(BLUE).sizeDp(ICON_SIZE);
        } else if (thisEvent.getEventType().equals("Birth")) {
            eventIcon = new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_map_marker)
                    .color(MAGENTA).sizeDp(ICON_SIZE);
        } else if (thisEvent.getEventType().equals("Death")){
            eventIcon = new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_map_marker)
                    .color(RED).sizeDp(ICON_SIZE);
        } else {
            eventIcon = new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_map_marker)
                    .sizeDp(ICON_SIZE);
        }
        icon.setImageDrawable(eventIcon);

    }
    public void bind(Person thisPerson) {
        this.person = thisPerson;
        name.setText(person.getFirstName() + " " + person.getLastName());

        id = person.getPersonId();

        Drawable genderIcon;
        if (thisPerson.getGender().equals("f")) {
            genderIcon = new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_female)
                    .color(WATERMELON).sizeDp(ICON_SIZE);
        }
        else {
            genderIcon = new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_male)
                    .color(BLUE).sizeDp(ICON_SIZE);
        }
        icon.setImageDrawable(genderIcon);
    }

    @Override
    public void onClick(View v) {

        //Is a person
        if (viewType == PERSON_SEARCH_ITEM) {
            Bundle extras = new Bundle();
            extras.putString("PERSON_ID",id);
            Intent intent = new Intent(v.getContext(), PersonActivity.class);

            intent.putExtras(extras);
            v.getContext().startActivity(intent);
        //Is an event
        } else {
            Bundle extras = new Bundle();
            extras.putString("EVENT_ID",id);
            Intent intent = new Intent(v.getContext(), EventActivity.class);

            intent.putExtras(extras);
            v.getContext().startActivity(intent);
        }
    }
}
