package com.luisfuture.movie.api.controller;

import com.luisfuture.movie.MovieApiApplication;
import com.luisfuture.movie.api.jpa.Category;
import com.luisfuture.movie.api.jpa.Movie;
import com.luisfuture.movie.api.repository.CategoryRepository;
import com.luisfuture.movie.api.repository.MovieRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by LuisFuture on 04/07/2016.
 * Project: MovieAPI
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringApplicationConfiguration( classes = MovieApiApplication.class )
@WebAppConfiguration
public class CategoryRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Movie movie;
    private Category category;


    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();
        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.movieRepository.deleteAllInBatch();
        this.category = categoryRepository.findByDescription("Action");
        this.movie = movieRepository.save(new Movie("Bryan Singer", "Marvel Entertainment", "X-Men: Apocalypse", 2016, 4.4, 0, category, new Date(), "luisfuture"));


    }


    @Test
    public void readCategory() throws Exception {

        mockMvc.perform(get("/categories/"+this.category.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("id", is((int) category.getId())))
                .andExpect(jsonPath("description", is(category.getDescription())));
    }

    @Test
    public void readMovies() throws Exception {
        mockMvc.perform(get("/categories/"+category.getId()+"/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("content[0].id", is((int) movie.getId())))
                .andExpect(jsonPath("content[0].title", is(movie.getTitle())));

    }

}