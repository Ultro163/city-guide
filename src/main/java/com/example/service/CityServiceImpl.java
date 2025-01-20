package com.example.service;

import com.example.model.City;
import com.example.model.EntityName;
import com.example.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Реализация сервиса для работы с городами.
 * Предоставляет операции CRUD для городов.
 */
@Slf4j
@Service
@Transactional
public class CityServiceImpl extends AbstractCrudService<City, Long> {

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        super(cityRepository, EntityName.CITY);
    }

    @Override
    public City create(final City city) {
        return super.create(city);
    }

    @Override
    @Transactional(readOnly = true)
    public City getById(final Long cityId) {
        return super.getById(cityId);
    }

    @Override
    public City update(final Long cityId, final City updateCity) {
        final City city = getById(cityId);
        Optional.ofNullable(updateCity.getName()).ifPresent(city::setName);
        Optional.ofNullable(updateCity.getCountry()).ifPresent(city::setCountry);
        return super.update(cityId, city);
    }

    @Override
    public void delete(final Long cityId) {
        super.delete(cityId);
    }
}
