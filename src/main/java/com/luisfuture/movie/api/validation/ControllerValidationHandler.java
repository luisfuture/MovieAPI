package com.luisfuture.movie.api.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
@ControllerAdvice
public class ControllerValidationHandler {

    @Autowired
    private MessageSource msgSource;

    @ExceptionHandler( MethodArgumentNotValidException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    @ResponseBody
    public MessageValidation processValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        FieldError error = result.getFieldError();

        return processFieldError(error);
    }

    private MessageValidation processFieldError(FieldError error) {
        MessageValidation message = null;
        if (error != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            String msg = msgSource.getMessage(error.getDefaultMessage(), null, currentLocale);
            message = new MessageValidation(msg, MessageType.ERROR);
        }
        return message;
    }
}
