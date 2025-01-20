package com.example.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для создания нового отзыва о достопримечательности.
 * Этот класс используется для передачи данных, необходимых для создания отзыва, включая комментарий, оценку,
 * идентификаторы автора отзыва и достопримечательности.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAttractionReviewDto {
    private String comment;
    @Min(1)
    @Max(5)
    private Integer rating;
    @NotNull
    private Long authorId;
    @NotNull
    private Long attractionId;
}