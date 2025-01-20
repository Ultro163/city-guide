package com.example.dto.attraction;

import com.example.dto.category.CategoryDto;
import com.example.dto.city.CityDto;
import com.example.dto.location.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для представления достопримечательности,
 * используемой при отображении отзывов. Этот класс используется для передачи
 * основной информации о достопримечательности (без рейтинга) пользователю.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDtoForReview {
    private Long id;
    private String name;
    private CategoryDto category;
    private LocationDto location;
    private CityDto city;
}