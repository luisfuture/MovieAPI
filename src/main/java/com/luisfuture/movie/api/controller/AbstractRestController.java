package com.luisfuture.movie.api.controller;

import com.luisfuture.movie.api.exception.EntityNotFountException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
public class AbstractRestController {
    private static final Logger logger = LogManager.getLogger(AbstractRestController.class);
    protected static final String  DEFAULT_PAGE_SIZE = "100";
    protected static final String DEFAULT_PAGE_NUM = "0";

    protected static <T> T checkResourceFound(final T resource, final String entity, final Long id) {
        if (resource == null) {
            logger.error("Error resource not found");
            throw new EntityNotFountException(entity, id);
        }
        return resource;
    }
}
