package com.luisfuture.movie.api.validation;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
public class MessageValidation {
    private String message;
    private MessageType type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageValidation(String message, MessageType type) {
        this.message = message;
        this.type = type;
    }
}
