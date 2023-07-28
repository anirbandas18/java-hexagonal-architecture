package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.service;

import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CuisineDetailsResponse;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data.CuisineException;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CreateCuisineRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CuisineService {

    public CuisineDetailsResponse retrieveDetailsById(String id, Optional<TOABCascadeLevel> optionalCascadeLevel) throws CuisineException;

    public String createCuisine(CreateCuisineRequest request) throws CuisineException;

}
