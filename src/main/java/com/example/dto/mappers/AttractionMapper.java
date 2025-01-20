package com.example.dto.mappers;

import com.example.dto.attraction.AttractionDto;
import com.example.dto.attraction.AttractionDtoForReview;
import com.example.dto.attraction.AttractionShortDto;
import com.example.dto.attraction.NewAttractionDto;
import com.example.dto.attraction.UpdateAttractionDto;
import com.example.dto.location.LocationDto;
import com.example.model.Attraction;
import com.example.model.Location;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {CategoryMapper.class, AttractionMapper.class, CityMapper.class})
public interface AttractionMapper {
    Location toEntity(LocationDto locationDto);

    Attraction toEntity(AttractionDto attractionDto);

    AttractionDto toAttractionDto(Attraction attraction);

    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "categoryId", target = "category.id")
    Attraction toEntity(NewAttractionDto newAttractionDto);

    @Mapping(source = "cityId", target = "city.id")
    @Mapping(source = "categoryId", target = "category.id")
    Attraction toEntity(UpdateAttractionDto updateAttractionDto);

    Attraction toEntity(AttractionDtoForReview attractionDtoForReview);

    Attraction toEntity(AttractionShortDto attractionShortDto);

    @InheritInverseConfiguration(name = "toEntity")
    AttractionShortDto toAttractionShortDto(Attraction attraction);
}
