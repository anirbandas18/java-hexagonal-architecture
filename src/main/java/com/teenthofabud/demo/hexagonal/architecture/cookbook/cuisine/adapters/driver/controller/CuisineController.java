package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driver.controller;

import com.teenthofabud.core.common.constant.TOABBaseMessageTemplate;
import com.teenthofabud.core.common.constant.TOABCascadeLevel;
import com.teenthofabud.core.common.data.vo.CreatedVo;
import com.teenthofabud.core.common.data.vo.ErrorVo;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CuisineRequest;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CuisineResponse;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.service.CuisineService;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.error.CookbookErrorCode;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data.CuisineException;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data.CuisineMessageTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("cuisine")
@Slf4j
@Tag(name = "Cuisine API", description = "Manage Cuisines and their details")
public class CuisineController {

    private static final String MEDIA_COOK_APPLICATION_JSON_PATCH = "application/json-patch+json";

    @Autowired
    public void setService(CuisineService service) {
        this.service = service;
    }

    private CuisineService service;

    @Operation(summary = "Create new Cuisine details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Id of newly created Cuisine",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CreatedVo.class)) }),
            @ApiResponse(responseCode = "400", description = "Cuisine attribute's value is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "409", description = "Cuisine already exists with the given attribute values",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "422", description = "No Cuisine attributes provided",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal system error while trying to create new Cuisine",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreatedVo postNewCuisine(@RequestBody(required = false) CuisineRequest form) throws CuisineException {
        log.debug("Requesting to create new cuisine");
        if(form != null) {
            String id = service.createCuisine(form);
            log.debug("Responding with identifier of newly created new cuisine");
            CreatedVo createdVo = new CreatedVo();
            createdVo.setId(id);
            return createdVo;
        }
        log.debug("CuisineRequest is null");
        throw new CuisineException(CookbookErrorCode.COOK_ATTRIBUTE_UNEXPECTED,
                new Object[]{ "form", TOABBaseMessageTemplate.MSG_TEMPLATE_NOT_PROVIDED });
    }

    @Operation(summary = "Get Cuisine details by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve the details of Cuisine that matches the given id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CuisineResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Cuisine id is invalid",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) }),
            @ApiResponse(responseCode = "404", description = "No Cuisine found with the given id",
                    content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorVo.class)) })
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public CuisineResponse getCuisineDetailsById(@PathVariable String id, @RequestParam(required = false)
    @io.swagger.v3.oas.annotations.Parameter(in = ParameterIn.QUERY, description = "levels of nested fields to be unfolded within the response body")
            String cascadeUntilLevel) throws CuisineException {
        CuisineResponse cuisineDetails = null;
        log.debug("Requesting all details of cuisine by its id");
        if(StringUtils.hasText(StringUtils.trimWhitespace(id)) && StringUtils.isEmpty(StringUtils.trimWhitespace(cascadeUntilLevel))) {
            cuisineDetails = service.retrieveDetailsById(id, Optional.empty());
            log.debug("Responding with successful retrieval of existing cuisine details by id");
            return cuisineDetails;
        } else if(StringUtils.hasText(StringUtils.trimWhitespace(id)) && StringUtils.hasText(StringUtils.trimWhitespace(cascadeUntilLevel))) {
            try {
                Integer cascadeLevelCode = Integer.parseInt(cascadeUntilLevel);
                if(cascadeLevelCode < 0) {
                    throw new NumberFormatException();
                }
                log.debug("Requested with cascade level code: {}", cascadeLevelCode);
                Optional<TOABCascadeLevel> optCascadeLevel = TOABCascadeLevel.findByLevelCode(cascadeUntilLevel);
                cuisineDetails = service.retrieveDetailsById(id, optCascadeLevel);
                log.debug("Responding with successful retrieval of existing cuisine details by id wth fields cascaded to given level");
                return cuisineDetails;
            } catch (NumberFormatException e) {
                log.debug(CuisineMessageTemplate.MSG_TEMPLATE_CUISINE_CASCADE_LEVEL_EMPTY.getValue());
                throw new CuisineException(CookbookErrorCode.COOK_ATTRIBUTE_INVALID, new Object[] { "cascadeUntilLevel", cascadeUntilLevel });
            }
        }
        log.debug(CuisineMessageTemplate.MSG_TEMPLATE_CUISINE_ID_EMPTY.getValue());
        throw new CuisineException(CookbookErrorCode.COOK_ATTRIBUTE_INVALID, new Object[] { "id", id });
    }

}
