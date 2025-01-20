package com.example.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для обновления отзыва о достопримечательности.
 * Этот класс используется для передачи данных при изменении существующего отзыва о достопримечательности.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAttractionReviewDto {
    private String comment;
    @Min(1)
    @Max(5)
    private Integer rating;
    @NotNull
    private Long authorId;
    @NotNull
    private Long attractionId;
}