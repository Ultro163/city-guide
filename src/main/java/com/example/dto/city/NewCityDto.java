package com.example.dto.city;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для создания нового города.
 * Этот класс используется для передачи данных при создании нового города.
 * Поля "name" и "country" обязательны для заполнения.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCityDto {
    @NotNull
    private String name;
    @NotNull
    private String country;
}