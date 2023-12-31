package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository.converter;

import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities.Cuisine;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CreateCuisineRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CuisineRequest2DefaultConverter implements Converter<CreateCuisineRequest, Cuisine> {

    private List<String> fieldsToEscape;

    @Value("#{'${res.cookbook.cuisine.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public Cuisine convert(CreateCuisineRequest form) {
        Cuisine entity = new Cuisine(form.getName(), form.getDescription());
        log.debug("Converting {} to {}", form, entity);
        return entity;
    }

}
