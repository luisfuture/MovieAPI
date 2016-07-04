package com.luisfuture.movie.api.repository;

import com.luisfuture.movie.api.jpa.Rating;
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
@RepositoryRestResource( collectionResourceRel = "rating", exported = false )
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Page<Rating> findByMovieId(Long id, Pageable pageable);

    @Query( "Select (sum(r.rating)*1.0)/count(r) from Rating r where r.movie.id=:id" )
    double calculateRating(@Param( "id" ) Long id);
}
