package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.teenthofabud.core.common.data.vo.TOABBaseVo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CuisineDetailsResponse extends TOABBaseVo implements Comparable<CuisineDetailsResponse> {

    @ToString.Include
    private String id;
    @EqualsAndHashCode.Include
    @ToString.Include
    private String name;
    @EqualsAndHashCode.Include
    @ToString.Include
    private String description;
    @Override
    public int compareTo(CuisineDetailsResponse o) {
        return Integer.compare(this.getName().compareTo(o.getName()), this.getDescription().compareTo(o.getDescription()));
    }
}
