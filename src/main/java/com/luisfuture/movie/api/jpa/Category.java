package com.luisfuture.movie.api.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by LuisFuture on 28/06/2016 .
 * Project: MovieAPI
 */

@Entity
@Table( name = "category_movie" )
@Proxy( lazy = false )
public class Category implements Serializable {

    @JsonIgnore
    @Version
    private int version;
    @Id
    private long id;
    @Column( length = 128 )
    private String description;
    @Column( name = "creation_date", nullable = false )
    private Date creationDate;

    @Column( name = "creation_by", nullable = false )
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category(long id, String description) {
        this.id = id;
        this.description = description;
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

    public Category(long id, String description, Date creationDate, String creationBy) {
        this.id = id;
        this.description = description;
        this.creationDate = creationDate;
        this.creationBy = creationBy;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return "Category{" +
                "version=" + version +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", creationBy='" + creationBy + '\'' +
                '}';
    }
}
