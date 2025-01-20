package com.example.dto.mappers;

import com.example.dto.city.CityDto;
import com.example.dto.city.NewCityDto;
import com.example.dto.city.UpdateCityDto;
import com.example.model.City;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {
    City toEntity(CityDto cityDto);

    CityDto toCityDto(City city);

    City toEntity(NewCityDto newCityDto);

    City toEntity(UpdateCityDto updateCityDto);
}