package com.fernando.notmuch.Model;

/**
 * Created by fernando on 20/11/17.
 */

public class Message {
    private String userId;
    private String message;

    public Message() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
