package com.luisfuture.movie.api.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by LuisFuture on 28/06/2016 .
 * Project: MovieAPI
 */
@Proxy( lazy = false )
@Entity
@Table( name = "movie" )
@SequenceGenerator( name = "movie_sq", sequenceName = "movie_sq" )
public class Movie implements Serializable {
    @JsonIgnore
    @Version
    private int version;

    @Id
    @GeneratedValue( generator = "movie_sq" )
    private long id;
    @NotNull( message = "error.director.notnull" )
    @Size( min = 1, max = 256, message = "error.director.size" )
    @Column( length = 256, nullable = false )
    private String director;
    @NotNull( message = "error.recording_studio.notnull" )
    @Size( min = 1, max = 512, message = "error.recording_studio.size" )
    @Column( name = "recording_studio", length = 512, nullable = false )
    private String recordingStudio;
    @NotNull( message = "error.title.notnull" )
    @Size( min = 1, max = 256, message = "error.title.size" )
    @Column( length = 256 )
    private String title;
    private int year;
    private double rating;
    private long ratingCount;
    @ManyToOne
    @JoinColumn( name = "id_category_movie" )
    private Category category;
    @Column( name = "creation_date", nullable = false )
    private Date creationDate;
    @NotNull( message = "error.creation_by.notnull" )
    @Size( min = 1, max =128 , message = "error.creation_by.size" )
    @Column( length =128 , name = "creation_by", nullable = false )
    private String creationBy;
    @JsonIgnore
    @OneToMany( mappedBy = "movie", cascade = CascadeType.REMOVE )
    private Set<Rating> ratingMovie = new HashSet<>();

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRecordingStudio() {
        return recordingStudio;
    }

    public void setRecordingStudio(String recordingStudio) {
        this.recordingStudio = recordingStudio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreationBy() {
        return creationBy;
    }

    public void setCreationBy(String creationBy) {
        this.creationBy = creationBy;
    }

    public Set<Rating> getRatingMovie() {
        return ratingMovie;
    }

    public void setRatingMovie(Set<Rating> ratingMovie) {
        this.ratingMovie = ratingMovie;
    }

    public long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Movie(String director, String recordingStudio, String title, int year, double rating, long ratingCount, Category category, Date creationDate, String creationBy) {
        this.director = director;
        this.recordingStudio = recordingStudio;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.category = category;
        this.creationDate = creationDate;
        this.creationBy = creationBy;
    }

    public Movie() {
    }

    @Override
    public String toString() {
        return "Movie{" +
                "version=" + version +
                ", id=" + id +
                ", director='" + director + '\'' +
                ", recordingStudio='" + recordingStudio + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                ", ratingCount=" + ratingCount +
                ", category=" + category +
                ", creationDate=" + creationDate +
                ", creationBy='" + creationBy + '\'' +
                '}';
    }
}
