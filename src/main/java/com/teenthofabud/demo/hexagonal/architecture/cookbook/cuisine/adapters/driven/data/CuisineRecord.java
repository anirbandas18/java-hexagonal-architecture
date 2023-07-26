package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data;

import com.teenthofabud.core.common.data.entity.TOABBaseEntity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cuisine")
@EntityListeners(AuditingEntityListener.class)
public class CuisineRecord extends TOABBaseEntity implements Comparable<CuisineRecord> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @Override
    public int compareTo(CuisineRecord o) {
        return this.getId().compareTo(o.getId());
    }
}
