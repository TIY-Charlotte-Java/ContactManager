package com.theironyard.charlotte;

/**
 * Created by Ben on 4/23/17.
 */
public class Event {
    private String eventId;

    public Event() {
    }

    public Event(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
