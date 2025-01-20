package com.example.dto.review;

import com.example.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для представления отзыва о достопримечательности.
 * Этот класс используется для передачи данных о отзыве, включая комментарий, оценку и информацию об авторе отзыва.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDtoForAttraction {
    private Long id;
    private String comment;
    private Integer rating;
    private UserDto author;
}