package com.luisfuture.movie.api.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by LuisFuture on 01/07/2016.
 * Project: MovieAPI
 */
@Proxy( lazy = false )
@Entity
@Table( name = "rating" )
@SequenceGenerator( name = "rating_sq", sequenceName = "rating_sq" )
public class Rating implements Serializable {
    @JsonIgnore
    @Version
    private int version;
    @Id
    @GeneratedValue( generator = "rating_sq" )
    private long id;
    @Max( value = 5, message = "error.rating.maxvalue" )
    @Min( value = 0, message = "error.rating.minvalue" )
    private int rating;
    @Temporal( TemporalType.TIMESTAMP )
    @JsonIgnore
    @ManyToOne
    @JoinColumn( name = "id_movie" )
    private Movie movie;
    @Size(  max = 2048, message = "error.comment.size" )
    @Column( length = 2048, nullable = false )
    private String comment;
    @Column( name = "creation_date", nullable = false )
    private Date creationDate;
    @NotNull( message = "error.creation_by.notnull" )
    @Size( min = 1, max = 256, message = "error.creation_by.size" )
    @Column( name = "creation_by", length = 256, nullable = false )
    private String creationBy;

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Rating() {
    }

    public Rating(int rating, Movie movie, String comment, Date creationDate, String creationBy) {
        this.rating = rating;
        this.movie = movie;
        this.comment = comment;
        this.creationDate = creationDate;
        this.creationBy = creationBy;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "version=" + version +
                ", id=" + id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", creationDate=" + creationDate +
                ", creationBy='" + creationBy + '\'' +
                '}';
    }
}
