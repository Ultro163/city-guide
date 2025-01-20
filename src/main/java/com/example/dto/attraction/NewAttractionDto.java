package com.example.dto.attraction;

import com.example.dto.location.LocationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для создания новой достопримечательности.
 * Этот класс используется для передачи данных при создании новой достопримечательности.
 * Содержит обязательные поля для имени, категории, местоположения и города.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAttractionDto {
    @NotNull
    @NotBlank
    @Size(max = 255)
    private String name;
    @NotNull
    private Long categoryId;
    @NotNull
    private LocationDto location;
    @NotNull
    private Long cityId;
}