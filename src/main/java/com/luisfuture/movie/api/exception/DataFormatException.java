package com.luisfuture.movie.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
@ResponseStatus( HttpStatus.CONFLICT)
public class DataFormatException extends RuntimeException{

    public DataFormatException(String entity, long id) {
        super(MessageFormat.format("The entity {0} not match with id {1}", new Object[]{entity, id}));
    }
}
