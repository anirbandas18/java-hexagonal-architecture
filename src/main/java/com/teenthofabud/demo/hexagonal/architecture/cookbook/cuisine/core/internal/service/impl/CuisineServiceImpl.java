package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.service.impl;

import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.data.dto.TOABRequestContextHolder;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository.converter.CuisineDefault2ResponseConverter;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.CuisineException;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.CuisineMessageTemplate;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository.converter.CuisineRequest2DefaultConverter;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities.Cuisine;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driven.CuisineRepository;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CreateCuisineRequest;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CuisineDetailsResponse;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.service.CuisineService;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.CookbookErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
@Slf4j
public class CuisineServiceImpl implements CuisineService {

    private CuisineRequest2DefaultConverter request2DefaultConverter;
    private CuisineDefault2ResponseConverter cuisineDefault2ResponseConverter;
    private CuisineRepository repository;


    @Autowired
    public CuisineServiceImpl(CuisineRequest2DefaultConverter request2DefaultConverter,
                              CuisineDefault2ResponseConverter cuisineDefault2ResponseConverter,
                              CuisineRepository repository) {
        this.request2DefaultConverter = request2DefaultConverter;
        this.cuisineDefault2ResponseConverter = cuisineDefault2ResponseConverter;
        this.repository = repository;
    }

    private Long parseCuisineId(String id) throws CuisineException {
        Long cuisineId = null;
        try {
            cuisineId = Long.parseLong(id);
            log.debug("Parsed id {} to cuisine id {} in numeric format", id, cuisineId);
            if(cuisineId <= 0) {
                throw new NumberFormatException("cuisine id can't be zero/negative");
            }
        } catch (NumberFormatException e) {
            log.error("Unable to parse cuisine id", e);
            log.debug(CuisineMessageTemplate.MSG_TEMPLATE_CUISINE_ID_INVALID.getValue(), id);
            throw new CuisineException(CookbookErrorCode.COOK_ATTRIBUTE_INVALID, new Object[] { "id", id });
        }
        return cuisineId;
    }

    @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
    @Override
    public CuisineDetailsResponse retrieveDetailsById(String id, Optional<TOABCascadeLevel> optionalCascadeLevel) throws CuisineException {
        log.info("Requesting Cuisine by id: {}", id);
        Long cuisineId = parseCuisineId(id);
        Optional<Cuisine> optEntity = repository.findById(cuisineId);
        if(optEntity.isEmpty()) {
            log.debug("No Cuisine found by id: {}", id);
            throw new CuisineException(CookbookErrorCode.COOK_NOT_FOUND, new Object[] { "id", String.valueOf(id) });
        }
        log.info("Found CuisineDetailsResponse by id: {}", id);
        Cuisine entity = optEntity.get();
        TOABCascadeLevel cascadeLevel = optionalCascadeLevel.isPresent() ? optionalCascadeLevel.get() : TOABCascadeLevel.ZERO;
        TOABRequestContextHolder.setCascadeLevelContext(cascadeLevel);
        CuisineDetailsResponse response = cuisineDefault2ResponseConverter.convert(entity);
        log.debug("CuisineDetailsResponse populated with fields cascaded to level: {}", cascadeLevel);
        TOABRequestContextHolder.clearCascadeLevelContext();
        return response;
    }

    @Transactional
    @Override
    public String createCuisine(CreateCuisineRequest request) throws CuisineException {
        log.info("Creating new Cuisine");

        if(request == null) {
            log.debug("CreateCuisineRequest provided is null");
            throw new CuisineException(CookbookErrorCode.COOK_ATTRIBUTE_UNEXPECTED,
                    new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
        }
        log.debug("Form details: {}", request);

        /**
         * Business logic: after 17:00 UTC throw exception
         */
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC); // TODO: move to a interface for unit testability using mock
        if(now.getHour() > 17) {
            throw new CuisineException(CookbookErrorCode.COOK_ACTION_FAILURE, new Object[] { "create", "not allowed after 17:00 UTC" });
        }

        Cuisine expectedEntity = request2DefaultConverter.convert(request);

        log.debug(CuisineMessageTemplate.MSG_TEMPLATE_CUISINE_EXISTENCE_BY_NAME.getValue(), request.getName());
        if(repository.existsByName(expectedEntity.getName())) {
            log.debug(CuisineMessageTemplate.MSG_TEMPLATE_CUISINE_EXISTS_BY_NAME.getValue(), expectedEntity.getName());
            throw new CuisineException(CookbookErrorCode.COOK_EXISTS,
                    new Object[]{ "name", request.getName() });
        }
        log.debug(CuisineMessageTemplate.MSG_TEMPLATE_CUISINE_NON_EXISTENCE_BY_NAME.getValue(), expectedEntity.getName());

        log.debug("Saving {}", expectedEntity);
        Cuisine actualEntity = repository.save(expectedEntity);
        log.debug("Saved {}", actualEntity);

        if(actualEntity == null) {
            log.debug("Unable to create {}", expectedEntity);
            throw new CuisineException(CookbookErrorCode.COOK_ACTION_FAILURE,
                    new Object[]{ "creation", "unable to persist CreateCuisineRequest details" });
        }
        log.info("Created new CreateCuisineRequest with id: {}", actualEntity.getId());
        return actualEntity.getId().toString();
    }

}