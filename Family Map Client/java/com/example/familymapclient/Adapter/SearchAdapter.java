package com.example.familymapclient.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familymapclient.Fragments.SearchViewHolder;
import com.example.familymapclient.Model.Event;
import com.example.familymapclient.Model.Person;
import com.example.familymapclient.R;

import java.util.List;
import java.util.Set;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private final List<Person> persons;
    private final List<Event> events;
    private static final int PERSON_SEARCH_ITEM = 0;
    private static final int EVENT_SEARCH_ITEM = 1;

    public SearchAdapter(List<Person> thesePersons, List<Event> theseEvents) {
        this.persons = thesePersons;
        this.events = theseEvents;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Is a person
        if (viewType == PERSON_SEARCH_ITEM) {
            view = li.inflate(R.layout.person_search_item, parent, false);
        } else {
            //Is an event
            view = li.inflate(R.layout.event_search_item, parent, false);

        }
        return new SearchViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return position < persons.size() ? PERSON_SEARCH_ITEM : EVENT_SEARCH_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        if (position < persons.size()) {
            holder.bind(persons.get(position));
        } else {
            holder.bind(events.get(position - persons.size()));
        }
    }

    @Override
    public int getItemCount() {
        return this.events.size() + this.persons.size();
    }

}
