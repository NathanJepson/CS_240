package com.example.familymapclient.Model;

import java.util.LinkedList;
import java.util.List;

public class StaticEventSorter {

    public StaticEventSorter() {}

    public static List<Event> sortedEvents(List<Event> theseEvents) {
        List<Event> result = new LinkedList<>();
        int iterator = -1; //Used to hold year of last-added event

        while (result.size() != theseEvents.size()) {
            int iterator2 = 65537; //Arbitrarily large magic number; outside of the range of known years--used...
            //...to find minimum next year
            int tag = -1; //This will grab index of what should be the next event

            for (int i=0; i < theseEvents.size(); i++) {
                boolean isInResultAlready = false;
                //Look for the minimum next year
                if (theseEvents.get(i).getYear() >= iterator) { //This assumes life events are at least 1 year apart
                    if (theseEvents.get(i).getYear() <= iterator2) {
                        Event thisEvent = theseEvents.get(i);
                        for (int j=0; j < result.size(); j++) {
                            if (result.get(j).getEventID().equals(thisEvent.getEventID())) {
                                isInResultAlready = true;
                            }
                        }
                        if (!isInResultAlready) {
                            iterator2 = theseEvents.get(i).getYear();
                            tag = i;
                        }
                    }
                }
            }
            if (tag != -1) {
                Event toAdd = theseEvents.get(tag);
                iterator = toAdd.getYear();
                result.add(toAdd);
            }
        }
        return result;
    }
}
