package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities;

import com.teenthofabud.core.common.error.TOABErrorCode;
import com.teenthofabud.core.common.error.TOABSystemException;
import org.springframework.util.StringUtils;

public class Cuisine {
    private Long id;
    private String name;
    private String description;
    private Boolean active;

    public Cuisine(Long id, String name, String description, Boolean active) {
        if((!StringUtils.isEmpty(StringUtils.trimWhitespace(name)) && name.length() < 3) || StringUtils.isEmpty(StringUtils.trimWhitespace(name))) {
            throw new TOABSystemException(TOABErrorCode.SYSTEM_INTERNAL_ERROR, new Object[] { "Cuisine name should be at least 3 characters long" });
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public Cuisine(String name, String description) {
        if((!StringUtils.isEmpty(StringUtils.trimWhitespace(name)) && name.length() < 3) || StringUtils.isEmpty(StringUtils.trimWhitespace(name))) {
            throw new TOABSystemException(TOABErrorCode.SYSTEM_INTERNAL_ERROR, new Object[] { "Cuisine name should be at least 3 characters long" });
        }
        this.name = name;
        this.description = description;
        this.active = true;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }
    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

}
