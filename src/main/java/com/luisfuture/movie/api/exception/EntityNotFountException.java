package com.luisfuture.movie.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
@ResponseStatus( HttpStatus.NOT_FOUND)
public class EntityNotFountException extends RuntimeException{

    public EntityNotFountException(String entity, long id) {
        super(MessageFormat.format("Could not find {0} with id {1}", new Object[]{entity, id}));
    }
}
