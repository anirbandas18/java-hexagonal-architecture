package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository;

import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data.CuisineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface CuisineJPARepository extends JpaRepository<CuisineRecord, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    List<CuisineRecord> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public CuisineRecord save(CuisineRecord entity);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Boolean existsByName(String name);
}
