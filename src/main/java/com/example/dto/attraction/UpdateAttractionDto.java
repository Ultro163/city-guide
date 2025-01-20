package com.example.dto.attraction;

import com.example.dto.location.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для обновления информации о достопримечательности.
 * Этот класс используется для передачи данных при обновлении существующей достопримечательности.
 * Все поля являются необязательными.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAttractionDto {
    private String name;
    private Long categoryId;
    private LocationDto location;
    private Long cityId;
}