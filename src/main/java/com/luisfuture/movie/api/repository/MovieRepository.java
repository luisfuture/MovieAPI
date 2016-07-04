package com.luisfuture.movie.api.repository;

import com.luisfuture.movie.api.jpa.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by LuisFuture on 01/07/2016.
 * Project: MovieAPI
 */
@RepositoryRestResource( collectionResourceRel = "movie", exported = false )
public interface MovieRepository extends JpaRepository<Movie, Long> {


    @Query( "Select m from Movie m where m.category.id=:id" )
    Page<Movie> findByCategoryId(@Param( value = "id" ) Long id, Pageable pageable);
}
