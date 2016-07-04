package com.luisfuture.movie.api.controller;

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
import org.springframework.web.bind.annotation.*;

/**
 * Created by LuisFuture on 04/07/2016.
 * Project: MovieAPI
 */
@RestController
@RequestMapping( "categories" )
@Api( value = "categories", description = "Movie API" )
public class CategoryRestController extends AbstractRestController {

    private static final Logger logger = LogManager.getLogger(MovieRestController.class);
    private final CategoryRepository categoryRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public CategoryRestController(CategoryRepository categoryRepository, MovieRepository movieRepository) {
        this.categoryRepository = categoryRepository;
        this.movieRepository = movieRepository;
    }


    @RequestMapping( method = RequestMethod.GET )
    @ApiOperation( value = "Get a paginated list of all categories", notes = "The list is paginated. You can provide a page number (default 0) and a page size (default 100)" )
    public Page<Category> readCategories(@ApiParam( value = "The page number (zero-based)", required = true )
                                         @RequestParam( value = "page", defaultValue = DEFAULT_PAGE_NUM ) Integer page,
                                         @ApiParam( value = "The page size", required = true )
                                         @RequestParam( value = "size", defaultValue = DEFAULT_PAGE_SIZE ) Integer size) {

        logger.debug("Start readCategories");
        logger.debug("Page :" + page);
        logger.debug("size :" + size);
        try {
            Sort.Direction direction = Sort.Direction.ASC;
            return categoryRepository.findAll(new PageRequest(page, size, direction, "description"));

        } finally {
            logger.debug("End readCategories");
        }

    }

    @RequestMapping( value = "/{categoryId}",
            method = RequestMethod.GET )
    @ApiOperation( value = "Get a single category.", notes = "You have to provide a valid category Id." )
    public Category readCategory(
            @ApiParam( value = "The category Id is required", required = true)
            @PathVariable( "categoryId" ) Long id) {
        logger.debug("Start readCategory");
        logger.debug("Id :" + id);
        try {
            Category category = categoryRepository.findOne(id);
            checkResourceFound(category, "Category Movie", id);
            return category;
        } finally {
            logger.debug("End readCategory");
        }
    }

    @RequestMapping( value = "/{categoryId}/movies", method = RequestMethod.GET )
    @ApiOperation( value = "Get a paginated list of all movies of the category", notes = "The list is paginated. You can provide a page number (default 0), a page size (default 100) and sort (default title asc)" )
    public Page<Movie> readMovies(@ApiParam( value = "The page number (zero-based)", required = true )
                                  @RequestParam( value = "page", defaultValue = DEFAULT_PAGE_NUM ) Integer page,
                                  @ApiParam( value = "The page size", required = true )
                                  @RequestParam( value = "size", defaultValue = DEFAULT_PAGE_SIZE ) Integer size,
                                  @ApiParam( value = "The page sort,if rate is desc else title asc", required = true )
                                          String sort,
                                  @ApiParam( value = "The category Id valid for read all movies", required = true )
                                      @PathVariable( value = "categoryId" ) Long categoryId) {

        logger.debug("Start readMovies");
        logger.debug("Page :" + page);
        logger.debug("size :" + size);
        logger.debug("sort :" + sort);
        try {
            Category category = categoryRepository.findOne(categoryId);
            checkResourceFound(category, "Category Movie", categoryId);
            Sort.Direction direction = Sort.Direction.ASC;
            if (sort != null && sort.trim().equals("rating")) {
                direction = Sort.Direction.DESC;
                return movieRepository.findByCategoryId(categoryId, new PageRequest(page, size, direction, sort));
            } else {
                return movieRepository.findByCategoryId(categoryId, new PageRequest(page, size, direction, "title"));
            }
        } finally {
            logger.debug("End readMovie");
        }

    }

}
