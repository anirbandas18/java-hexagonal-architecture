package com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.repository;

import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.adapters.driven.data.CuisineRecord;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.converter.CuisineDefault2RecordConverter;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.entities.Cuisine;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.ports.driven.CuisineRepository;
import com.teenthofabud.demo.hexagonal.architecture.cookbook.cuisine.core.internal.converter.CuisineRecord2DefaultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuisineRepositoryImpl implements CuisineRepository {


    private CuisineJPARepository cuisineJPARepository;
    private CuisineDefault2RecordConverter cuisineDefault2RecordConverter;
    private CuisineRecord2DefaultConverter cuisineRecord2DefaultConverter;

    @Autowired
    public CuisineRepositoryImpl(CuisineJPARepository cuisineJPARepository, CuisineDefault2RecordConverter cuisineDefault2RecordConverter, CuisineRecord2DefaultConverter cuisineRecord2DefaultConverter) {
        this.cuisineJPARepository = cuisineJPARepository;
        this.cuisineDefault2RecordConverter = cuisineDefault2RecordConverter;
        this.cuisineRecord2DefaultConverter = cuisineRecord2DefaultConverter;
    }

    @Override
    public List<Cuisine> findByName(String name) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Cuisine save(Cuisine entity) {
        CuisineRecord cuisineRecord = cuisineDefault2RecordConverter.convert(entity);
        cuisineRecord = this.cuisineJPARepository.save(cuisineRecord);
        entity = cuisineRecord2DefaultConverter.convert(cuisineRecord);
        return entity;
    }

    @Override
    public Boolean existsByName(String name) {
        return this.cuisineJPARepository.existsByName(name);
    }

    @Override
    public List<Cuisine> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Optional<Cuisine> findById(Long cuisineId) {
        Optional<CuisineRecord> cuisineEntity = this.cuisineJPARepository.findById(cuisineId);
        Optional<Cuisine> entity = cuisineEntity.isPresent() ?
                Optional.of(cuisineRecord2DefaultConverter.convert(cuisineEntity.get())) : Optional.empty();
        return entity;
    }
}
