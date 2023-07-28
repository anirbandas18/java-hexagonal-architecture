package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository.converter;

import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository.data.CuisineRecord;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities.Cuisine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CuisineRecord2DefaultConverter implements Converter<CuisineRecord, Cuisine> {

    private List<String> fieldsToEscape;

    @Value("#{'${res.cookbook.cuisine.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public Cuisine convert(CuisineRecord entity) {
        Cuisine vo = new Cuisine(entity.getId(), entity.getName(), entity.getDescription(), entity.getActive());
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }

}
