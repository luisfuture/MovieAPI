package com.luisfuture.movie.api.repository;

import com.luisfuture.movie.api.jpa.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by LuisFuture on 01/07/2016.
 * Project: MovieAPI
 */
@RepositoryRestResource( collectionResourceRel = "category", exported = false )
public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByDescription(String description);


}
