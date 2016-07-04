package com.luisfuture.movie.api.controller;


import com.luisfuture.movie.api.exception.DataFormatException;
import com.luisfuture.movie.api.jpa.Movie;
import com.luisfuture.movie.api.jpa.Rating;
import com.luisfuture.movie.api.repository.MovieRepository;
import com.luisfuture.movie.api.repository.RatingRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
@RestController
@RequestMapping( "movies/{movieId}/ratings" )
@Api( value = "ratings", description = "Movie API" )
public class RatingRestController extends AbstractRestController {
    protected final Logger logger = LogManager.getLogger(RatingRestController.class);
    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;


    @Autowired
    public RatingRestController(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
    }


    @RequestMapping( method = RequestMethod.POST )
    @ApiOperation( value = "Create a rating resource.", notes = "Returns the URL of the new rating." )
    public ResponseEntity<?> createRating(
            @ApiParam( value = "The ID of the existing movie." )
            @PathVariable( value = "movieId" ) Long movieId, @Validated @RequestBody Rating rating) {
        logger.debug("Start createRating");
        logger.debug("movieId :" + movieId);
        logger.debug("rating :" + rating);
        try {

            Movie movie = this.movieRepository.findOne(movieId);
            checkResourceFound(movie, "Movie", movieId);


            Rating ratingSave = new Rating();
            ratingSave.setComment(rating.getComment());
            ratingSave.setMovie(movie);
            ratingSave.setCreationDate(new Date());
            ratingSave.setRating(rating.getRating());
            ratingSave.setCreationBy(rating.getCreationBy());


            logger.debug("Save new rating :" + ratingSave);
            ratingSave = ratingRepository.saveAndFlush(ratingSave);

            double calculateRating = ratingRepository.calculateRating(movieId);

            movie.setRating(calculateRating);
            movie.setRatingCount(movie.getRatingCount() + 1);

            logger.debug("Save movie :" + movie);
            movieRepository.saveAndFlush(movie);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{ratingId}")
                    .buildAndExpand(ratingSave.getId()).toUri());
            return new ResponseEntity<>(ratingSave, httpHeaders, HttpStatus.CREATED);
        } finally {
            logger.debug("End createRating");
        }

    }

    @RequestMapping( value = "/{ratingId}", method = RequestMethod.PUT )
    @ApiOperation( value = "Update a rating resource.", notes = "Returns the URL of the rating." )
    public ResponseEntity<?> updateRating(
            @ApiParam( value = "The ID of the existing movie.",required =  true)
            @PathVariable( value = "movieId" ) Long movieId,
            @ApiParam( value = "The ID of the existing rating", required = true )
            @PathVariable( "ratingId" ) Long ratingId,
            @Validated @RequestBody Rating rating) {
        logger.debug("Start updateRating");
        logger.debug("movieId :" + movieId);
        logger.debug("ratingId :" + ratingId);
        logger.debug("rating :" + rating);
        try {

            Movie movie = this.movieRepository.findOne(movieId);
            checkResourceFound(movie, "Movie", movieId);


            Rating ratingUpdate = ratingRepository.findOne(ratingId);
            checkResourceFound(ratingUpdate, "Movie", ratingId);
            if (ratingId != rating.getId()) {
                logger.error("The Id no match with rating Id");
                throw new DataFormatException("Rating", ratingId);
            }


            ratingUpdate.setComment(rating.getComment());
            ratingUpdate.setMovie(movie);

            ratingUpdate.setRating(rating.getRating());


            logger.debug("Update rating :" + ratingUpdate);
            ratingUpdate = ratingRepository.saveAndFlush(ratingUpdate);
            double calculateRating = ratingRepository.calculateRating(movieId);
            movie.setRating(calculateRating);
            logger.debug("Save movie :" + movie);
            movieRepository.saveAndFlush(movie);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(ServletUriComponentsBuilder
                    .fromCurrentRequest().path("")
                    .buildAndExpand().toUri());
            return new ResponseEntity<>(ratingUpdate, httpHeaders, HttpStatus.CREATED);
        } finally {
            logger.debug("End updateRating");
        }

    }


    @RequestMapping( value = "/{ratingId}", method = RequestMethod.GET )
    @ApiOperation( value = "Get a single Rating.", notes = "You have to provide a valid Rating Id." )
    public Rating readRating(@ApiParam( value = "The ID of the existing movie." ,required = true)
                             @PathVariable( value = "movieId" ) Long movieId,
                             @ApiParam( value = "The ID of the existing rating", required = true )
                             @PathVariable( "ratingId" ) Long ratingId) {

        logger.debug("Start readRating");
        logger.debug("movieId :" + movieId);
        logger.debug("ratingId :" + ratingId);
        try {

            Movie movie = this.movieRepository.findOne(movieId);
            checkResourceFound(movie, "Movie", movieId);

            Rating rating = this.ratingRepository.findOne(ratingId);
            checkResourceFound(rating, "Rating", ratingId);

            return rating;
        } finally {
            logger.debug("End readRating");
        }
    }

    @RequestMapping( method = RequestMethod.GET )
    @ApiOperation( value = "Get a paginated list of all ratings", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)" )
    public Page<Rating> readRatings(@ApiParam( value = "The page number (zero-based)", required = true )
                                    @RequestParam( value = "page", defaultValue = DEFAULT_PAGE_NUM ) Integer page,
                                    @ApiParam( value = "The page size", required = true )
                                    @RequestParam( value = "size", defaultValue = DEFAULT_PAGE_SIZE ) Integer size,
                                    @ApiParam( value = "The ID of the existing movie." ,required = true)
                                    @PathVariable( value = "movieId" ) Long movieId) {

        logger.debug("Start readRatings");
        logger.debug("movieId :" + movieId);

        try {
            Movie movie = this.movieRepository.findOne(movieId);
            checkResourceFound(movie, "Movie", movieId);
            return this.ratingRepository.findByMovieId(movieId, new PageRequest(page, size));
        } finally {
            logger.debug("End readRatings");
        }
    }


    @RequestMapping( value = "/{ratingId}",
            method = RequestMethod.DELETE )
    @ApiOperation( value = "Delete a rating resource.", notes = "You have to provide a valid rating ID in the URL. Once deleted the resource can not be recovered." )
    public void deleteRating(@ApiParam( value = "The ID of the existing rating resource.", required = true )
                             @PathVariable( "ratingId" ) Long ratingId,
                             @ApiParam( value = "The ID of the existing movie." ,required = true)
                             @PathVariable( value = "movieId" ) Long movieId) {

        logger.debug("Start deleteRating");
        logger.debug("Id :" + ratingId);
        try {
            Movie movie = this.movieRepository.findOne(movieId);
            checkResourceFound(movie, "Movie", movieId);

            checkResourceFound(ratingRepository.findOne(ratingId), "Rating", ratingId);
            ratingRepository.delete(ratingId);

            double calculateRating = ratingRepository.calculateRating(movieId);

            movie.setRating(calculateRating);
            movie.setRatingCount(movie.getRatingCount() - 1);

            logger.debug("Save movie :" + movie);
            movieRepository.saveAndFlush(movie);

        } finally {
            logger.debug("End deleteRating");
        }
    }

}
