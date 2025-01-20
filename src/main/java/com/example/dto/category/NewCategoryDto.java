package com.example.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для создания новой категории.
 * Этот класс используется для передачи данных при создании новой категории.
 * Поле "name" обязательно для заполнения и должно соответствовать ограничениям по длине.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotNull
    @Size(max = 50)
    private String name;
}