package com.luisfuture.movie.api.controller;

import com.luisfuture.movie.api.exception.DataFormatException;
import com.luisfuture.movie.api.exception.EntityNotFountException;
import com.luisfuture.movie.api.jpa.Category;
import com.luisfuture.movie.api.jpa.Movie;
import com.luisfuture.movie.api.repository.CategoryRepository;
import com.luisfuture.movie.api.repository.MovieRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping( "movies" )
@Api( value = "movies", description = "Movie API" )
public class MovieRestController extends AbstractRestController {
    private static final Logger logger = LogManager.getLogger(MovieRestController.class);
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieRestController(CategoryRepository categoryRepository, MovieRepository movieRepository) {
        this.categoryRepository = categoryRepository;
        this.movieRepository = movieRepository;
    }


    @RequestMapping( method = RequestMethod.POST )
    @ApiOperation( value = "Create a movie resource.", notes = "Returns the URL of the new movie." )
    public ResponseEntity<?> createMovie(
            @Validated @RequestBody Movie movie) {
        logger.debug("Start : creationMovie");
        logger.debug(" Object :" + movie);
        try {
            Category category;
            if (movie.getCategory() != null) {
                category = categoryRepository.getOne(movie.getCategory().getId());
                checkResourceFound(category, "Category Movie", movie.getCategory().getId());

            } else {
                logger.error("Category Movie Id is null");
                throw new EntityNotFountException("Category Movie", 0);
            }
            Movie movieSave = new Movie();
            movieSave.setYear(movie.getYear());
            movieSave.setRecordingStudio(movie.getRecordingStudio());
            movieSave.setDirector(movie.getDirector());
            movieSave.setTitle(movie.getTitle());
            movieSave.setCreationDate(new Date());
            movieSave.setCategory(category);
            movieSave.setCreationBy(movie.getCreationBy());
            logger.debug(" Save new  Movie:" + movieSave);
            movieSave = movieRepository.saveAndFlush(movieSave);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setLocation(ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{movieId}")
                    .buildAndExpand(movieSave.getId()).toUri());


            return new ResponseEntity<>(movieSave, httpHeaders, HttpStatus.CREATED);
        } finally {
            logger.debug("End : createMovie");
        }
    }

    @RequestMapping( value = "/{movieId}",
            method = RequestMethod.PUT )
    @ApiOperation( value = "Update a Movie resource.", notes = "You have to provide a valid movie Id in the URL. The ID attribute can not be updated." )
    public ResponseEntity<?> updateMovie(@ApiParam( value = "The Id of the existing movie.", required = true )
                                         @PathVariable( "movieId" ) Long movieId,
                                         @Validated @RequestBody Movie movie) {

        logger.debug("Star updateMovie");
        logger.debug("movieId :" + movieId);
        logger.debug("Movie :" + movie);
        try {
            Movie movieUpdate = movieRepository.findOne(movieId);
            checkResourceFound(movieUpdate, "Movie", movieId);
            if (movieId != movie.getId()) {
                logger.error("The Id no match with Movie Id");
                throw new DataFormatException("Movie", movieId);
            }

            Category category;

            if (movie.getCategory() != null) {
                category = categoryRepository.getOne(movie.getCategory().getId());
                checkResourceFound(category, "Category Movie", movie.getCategory().getId());
                movieUpdate.setCategory(category);
            }

            movieUpdate.setDirector(movie.getDirector());
            movieUpdate.setTitle(movie.getTitle());
            movieUpdate.setRecordingStudio(movie.getRecordingStudio());
            movieUpdate.setYear(movie.getYear());

            logger.debug("Update Movie :" + movieUpdate);
            movieUpdate = movieRepository.saveAndFlush(movieUpdate);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.setLocation(ServletUriComponentsBuilder
                    .fromCurrentRequest().path("")
                    .buildAndExpand().toUri());
            return new ResponseEntity<>(movieUpdate, httpHeaders, HttpStatus.CREATED);
        } finally {
            logger.debug("End updateMovie");
        }
    }


    @RequestMapping( method = RequestMethod.GET )
    @ApiOperation( value = "Get a paginated list of all movies", notes = "The list is paginated. You can provide a page number (default 0), a page size (default 100) and sort (default title asc)" )
    public Page<Movie> readMovies(@ApiParam( value = "The page number (zero-based)", required = true )
                                  @RequestParam( value = "page", defaultValue = DEFAULT_PAGE_NUM ) Integer page,
                                  @ApiParam( value = "The page size", required = true )
                                  @RequestParam( value = "size", defaultValue = DEFAULT_PAGE_SIZE ) Integer size,
                                  @ApiParam( value = "The page sort,if rate is desc else title asc", required = true )
                                          String sort) {

        logger.debug("Start readMovies");
        logger.debug("Page :" + page);
        logger.debug("size :" + size);
        logger.debug("sort :" + sort);
        try {
            Sort.Direction direction = Sort.Direction.ASC;
            if (sort != null && sort.trim().equals("rating")) {
                direction = Sort.Direction.DESC;
                return movieRepository.findAll(new PageRequest(page, size, direction, sort));
            } else {
                return movieRepository.findAll(new PageRequest(page, size, direction, "title"));
            }
        } finally {
            logger.debug("End readMovie");
        }

    }

    @RequestMapping( value = "/{movieId}",
            method = RequestMethod.GET )
    @ApiOperation( value = "Get a single Movie.", notes = "You have to provide a valid Movie Id." )
    public Movie readMovie(
            @ApiParam( value = "The ID of the existing Movie resource", required = true )
            @PathVariable( "movieId" ) Long movieId) {
        logger.debug("Start readMovie");
        logger.debug("movieId :" + movieId);
        try {
            Movie movie = this.movieRepository.findOne(movieId);
            checkResourceFound(movie, "Movie", movieId);
            return movie;
        } finally {
            logger.debug("End readMovie");
        }
    }


    @RequestMapping( value = "/{movieId}",
            method = RequestMethod.DELETE )
    @ApiOperation( value = "Delete a Movie resource.", notes = "You have to provide a valid movie ID in the URL. Once deleted the resource can not be recovered." )
    public void deleteMovie(@ApiParam( value = "The ID of the existing Movie resource.", required = true )
                            @PathVariable( "movieId" ) Long movieId) {

        logger.debug("Start deleteMovie");
        logger.debug("Id :" + movieId);
        try {
            checkResourceFound(movieRepository.findOne(movieId), "Movie", movieId);
            movieRepository.delete(movieId);
        } finally {
            logger.debug("End deleteMovie");
        }
    }

}
