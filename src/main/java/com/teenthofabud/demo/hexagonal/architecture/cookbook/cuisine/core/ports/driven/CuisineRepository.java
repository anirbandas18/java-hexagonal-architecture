package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driven;

import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities.Cuisine;

import java.util.List;
import java.util.Optional;


public interface CuisineRepository {

    List<Cuisine> findByName(String name);

    Cuisine save(Cuisine entity);

    Boolean existsByName(String name);

    List<Cuisine> findAll();

    Optional<Cuisine> findById(Long cuisineId);
}
