package com.example.util;

import com.example.model.AttractionReview;

import java.util.List;

/**
 * Утилитный класс для вычисления среднего рейтинга аттракциона на основе отзывов.
 * Содержит метод для вычисления средней оценки из списка отзывов.
 * <p>
 * Этот класс является утилитным, и его экземпляры не могут быть созданы.
 *
 * @see AttractionReview
 */
public class CalculateRating {

    private CalculateRating() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Вычисляет средний рейтинг на основе списка отзывов.
     * <p>
     * Если список пуст или равен null, возвращает 0.0. Оценки, равные null, игнорируются.
     * Средний рейтинг округляется до одного знака после запятой.
     *
     * @param reviews Список отзывов для вычисления рейтинга
     * @return Средний рейтинг, округленный до одного знака после запятой. Если нет отзывов, возвращает 0.0.
     */
    public static Double calculateRating(List<AttractionReview> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        double rating = reviews.stream()
                .filter(review -> review.getRating() != null)
                .mapToInt(AttractionReview::getRating)
                .average()
                .orElse(0);

        return Math.round(rating * 10) / 10.0;
    }
}