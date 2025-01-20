package com.example.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link com.example.model.City}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCityDto {
    private String name;
    private String country;
}