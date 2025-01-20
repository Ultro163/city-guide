package com.example.dto.attraction;

import com.example.dto.category.CategoryDto;
import com.example.dto.city.CityDto;
import com.example.dto.location.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для представления достопримечательности.
 * Используется для передачи информации и рейтинга достопримечательности.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDto {
    private Long id;
    private String name;
    private CategoryDto category;
    private LocationDto location;
    private CityDto city;
    private Double rating;
}