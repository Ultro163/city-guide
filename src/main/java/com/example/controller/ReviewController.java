package com.example.controller;

import com.example.dto.mappers.AttractionReviewMapper;
import com.example.dto.review.AttractionReviewDto;
import com.example.dto.review.NewAttractionReviewDto;
import com.example.dto.review.ReviewDtoForAttraction;
import com.example.dto.review.UpdateAttractionReviewDto;
import com.example.model.AttractionReview;
import com.example.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для управления отзывами о достопримечательностях.
 * Предоставляет API для создания, обновления, удаления и получения отзывов.
 */
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final AttractionReviewMapper attractionReviewMapper;
    private final ReviewService reviewService;

    /**
     * Создает новый отзыв о достопримечательности.
     *
     * @param userId идентификатор пользователя, оставляющего отзыв.
     * @param attId  идентификатор достопримечательности, к которой относится отзыв.
     * @param dto    объект {@link NewAttractionReviewDto}, содержащий данные для создания отзыва.
     * @return объект {@link AttractionReviewDto}, представляющий полный отзыв.
     */
    @PostMapping("/user/{userId}/attraction/{attId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AttractionReviewDto createReview(@PathVariable long userId,
                                            @PathVariable long attId,
                                            @RequestBody @Valid NewAttractionReviewDto dto) {
        AttractionReview review = attractionReviewMapper.toEntity(dto);
        review.getAuthor().setId(userId);
        review.getAttraction().setId(attId);
        return attractionReviewMapper.toAttractionReviewDto(reviewService.create(review));
    }

    /**
     * Возвращает отзыв по его идентификатору.
     *
     * @param reviewId идентификатор отзыва.
     * @return объект {@link AttractionReviewDto}, содержащий полную информацию о отзыве.
     */
    @GetMapping("/{reviewId}")
    public AttractionReviewDto getReviewById(@PathVariable long reviewId) {
        return attractionReviewMapper.toAttractionReviewDto(reviewService.getById(reviewId));
    }

    /**
     * Обновляет информацию о существующем отзыве.
     *
     * @param reviewId идентификатор отзыва.
     * @param dto      объект {@link UpdateAttractionReviewDto}, содержащий обновленные данные отзыва.
     * @return объект {@link AttractionReviewDto}, представляющий обновленный отзыв.
     */
    @PatchMapping("/{reviewId}")
    public AttractionReviewDto updateReview(@PathVariable long reviewId,
                                            @RequestBody @Valid UpdateAttractionReviewDto dto) {
        AttractionReview review = attractionReviewMapper.toEntity(dto);
        return attractionReviewMapper.toAttractionReviewDto(reviewService.update(reviewId, review));
    }

    /**
     * Удаляет отзыв по его идентификатору.
     *
     * @param reviewId идентификатор отзыва.
     */
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.delete(reviewId);
    }

    /**
     * Получает список отзывов для указанной достопримечательности.
     *
     * @param attId         идентификатор достопримечательности, для которой нужно получить отзывы.
     * @param sortDirection направление сортировки отзывов (по умолчанию "desc").
     * @return список объектов {@link ReviewDtoForAttraction}, содержащих отзывы о достопримечательности.
     */
    @GetMapping("/attraction/{attId}")
    public List<ReviewDtoForAttraction> getReviewsByAttId(@PathVariable long attId,
                                                          @RequestParam(defaultValue = "desc") String sortDirection) {
        return reviewService.getReviewForAttraction(attId, sortDirection).stream()
                .map(attractionReviewMapper::toReviewDtoForAttraction).toList();
    }
}