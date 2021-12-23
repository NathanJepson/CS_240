package com.example.familymapclient.Response;

import com.example.familymapclient.Model.Event;


public class EventResponse {


    private Event[] data;
    private String message;
    private boolean success;

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
