package com.luisfuture.movie.api.controller;

import com.luisfuture.movie.MovieApiApplication;
import com.luisfuture.movie.api.jpa.Category;
import com.luisfuture.movie.api.jpa.Movie;
import com.luisfuture.movie.api.jpa.Rating;
import com.luisfuture.movie.api.repository.CategoryRepository;
import com.luisfuture.movie.api.repository.MovieRepository;
import com.luisfuture.movie.api.repository.RatingRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class RatingRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Movie movie;
    private Rating rate;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RatingRepository ratingRepository;
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


        Category category = categoryRepository.findByDescription("Action");
        this.movie = movieRepository.saveAndFlush(new Movie("Bryan Singer", "Marvel Entertainment", "X-Men: Apocalypse", 2016, 4.4, 0, category, new Date(), "luisfuture"));

        this.rate= ratingRepository.saveAndFlush(new Rating(4,movie,"",new Date(),"luisfuture"));

    }

    @Test
    public void movieNotFound() throws Exception {
        mockMvc.perform(get("/movies/5/rating/"+rate.getId())
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createRating() throws Exception {
       Rating rating= new Rating(4,movie,"Excelente",new Date(),"luisfuture");
        String rateJson = json(rating);
        this.mockMvc.perform(post("/movies/" + movie.getId()+"/ratings")
                .contentType(contentType)
                .content(rateJson))
                .andExpect(status().isCreated());

    }

    @Test
    public void updateRating() throws Exception {
        rate.setRating(5);
        String rateJson = json(rate);
        this.mockMvc.perform(put("/movies/" + movie.getId()+"/ratings/"+rate.getId())
                .contentType(contentType)
                .content(rateJson))
                .andExpect(status().isCreated());

    }

    @Test
    public void readRating() throws Exception {

        mockMvc.perform(get("/movies/" + this.movie.getId()+"/ratings/"+rate.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("id", is((int) rate.getId())))
                .andExpect(jsonPath("rating", is(rate.getRating())));

    }

    @Test
    public void readRatings() throws Exception {

        mockMvc.perform(get("/movies/"+movie.getId()+"/ratings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("content[0].id", is(((int)rate.getId()))))
                .andExpect(jsonPath("content[0].rating", is(rate.getRating())));

    }

    @Test
    public void deleteRating() throws Exception {

    }
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}