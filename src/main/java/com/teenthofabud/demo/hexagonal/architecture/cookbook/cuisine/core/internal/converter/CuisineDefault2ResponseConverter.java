package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.converter;

import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities.Cuisine;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto.CuisineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CuisineDefault2ResponseConverter implements Converter<Cuisine, CuisineResponse> {

    private List<String> fieldsToEscape;

    @Value("#{'${res.cookbook.cuisine.fields-to-escape}'.split(',')}")
    public void setFieldsToEscape(List<String> fieldsToEscape) {
        this.fieldsToEscape = fieldsToEscape;
    }

    @Override
    public CuisineResponse convert(Cuisine entity) {
        CuisineResponse vo = new CuisineResponse();
        if(!fieldsToEscape.contains("id")) {
            vo.setId(entity.getId().toString());
        }
        if(!fieldsToEscape.contains("name")) {
            vo.setName(entity.getName());
        }
        if(!fieldsToEscape.contains("description")) {
            vo.setDescription(entity.getDescription());
        }
        log.debug("Converted {} to {} ", entity, vo);
        return vo;
    }

}
