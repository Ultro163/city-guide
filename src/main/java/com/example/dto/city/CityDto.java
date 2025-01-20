package com.example.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для представления города.
 * Этот класс используется для передачи информации о городе, такой как его идентификатор, название и страна.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    private Long id;
    private String name;
    private String country;
}