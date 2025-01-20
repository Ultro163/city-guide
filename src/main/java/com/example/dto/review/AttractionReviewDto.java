package com.example.dto.review;

import com.example.dto.attraction.AttractionDtoForReview;
import com.example.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) для представления отзыва о достопримечательности.
 * Этот класс используется для передачи данных отзыва, включая комментарий, оценку, информацию о пользователе и достопримечательности.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttractionReviewDto {
    private Long id;
    private String comment;
    private Integer rating;
    private UserDto author;
    private AttractionDtoForReview attraction;
}