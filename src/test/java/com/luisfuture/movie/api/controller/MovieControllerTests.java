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
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Created by LuisFuture on 03/07/2016.
 * Project: MovieAPI
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringApplicationConfiguration( classes = MovieApiApplication.class )
@WebAppConfiguration
public class MovieControllerTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private Movie movie;
    private Movie movie2;

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
        Category category = categoryRepository.findByDescription("Action");
        this.movie = movieRepository.save(new Movie("Bryan Singer", "Marvel Entertainment", "X-Men: Apocalypse", 2016, 4.4, 0, category, new Date(), "luisfuture"));

        category = categoryRepository.findByDescription("Sci-Fi");
        this.movie2 = movieRepository.save(new Movie("Zack Snyder", "DC Entertainment", "Batman v Superman: Dawn of Justice", 2016, 4.4, 0, category, new Date(), "luisfuture"));

    }

    @Test
    public void readSingleMovie() throws Exception {
        mockMvc.perform(get("/movies/" + this.movie.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("id", is((int) movie.getId())))
                .andExpect(jsonPath("title", is(movie.getTitle())));
    }


    @Test
    public void movieNotFound() throws Exception {
        mockMvc.perform(get("/movies/5")
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }


    @Test
    public void readMovies() throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("content", hasSize(2)))
                .andExpect(jsonPath("content[0].id", is((int) movie2.getId())))
                .andExpect(jsonPath("content[0].title", is(movie2.getTitle())))
                .andExpect(jsonPath("content[1].id", is((int) movie.getId())))
                .andExpect(jsonPath("content[1].title", is(movie.getTitle())));
    }

    @Test
    public void createMovie() throws Exception {
        Category category = categoryRepository.findByDescription("Sci-Fi");
        String movieJson = json(new Movie("Tim Miller", "Marvel Entertainment", "Deadpool",
                2016, 4.4, 0, category, new Date(), "luisfuture"));

        this.mockMvc.perform(post("/movies")
                .contentType(contentType)
                .content(movieJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateMovie() throws Exception {

        String movieJson = json(movie);
        this.mockMvc.perform(put("/movies/" + movie.getId())
                .contentType(contentType)
                .content(movieJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteMovie() throws Exception {

        this.mockMvc.perform(delete("/movies/" + movie.getId())
                .contentType(contentType))
                .andExpect(status().isOk());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
