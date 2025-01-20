package com.example.dto.attraction;

import com.example.dto.category.CategoryDto;
import com.example.dto.city.CityDto;
import com.example.dto.location.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для представления краткой информации о достопримечательности.
 * Этот класс используется для передачи базовых данных о достопримечательности без рейтинга.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionShortDto {
    private Long id;
    private String name;
    private CategoryDto category;
    private LocationDto location;
    private CityDto city;
}