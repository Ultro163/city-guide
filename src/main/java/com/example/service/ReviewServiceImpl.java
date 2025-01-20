package com.example.service;

import com.example.error.exception.AccessDeniedException;
import com.example.error.exception.ValidationException;
import com.example.model.Attraction;
import com.example.model.AttractionReview;
import com.example.model.EntityName;
import com.example.model.User;
import com.example.repository.AttractionReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса для работы с отзывами о достопримечательностях.
 * Предоставляет операции CRUD для отзывов и дополнительные методы для работы с отзывами.
 */
@Slf4j
@Service
@Transactional
public class ReviewServiceImpl extends AbstractCrudService<AttractionReview, Long> implements ReviewService {
    private final UserServiceImpl userService;
    private final AttractionService attractionService;
    private final AttractionReviewRepository reviewRepository;

    protected ReviewServiceImpl(AttractionReviewRepository repository, UserServiceImpl userService,
                                AttractionService attractionServiceImpl, AttractionReviewRepository reviewRepository) {
        super(repository, EntityName.REVIEW);
        this.userService = userService;
        this.attractionService = attractionServiceImpl;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public AttractionReview create(final AttractionReview entity) {
        AttractionReview review = reviewRepository.findByAttractionIdAndAuthorId(entity.getAttraction().getId(),
                entity.getAuthor().getId()).orElse(null);
        if (review != null) {
            log.warn("User with ID {} is attempting to create a review for Attraction with ID {} that already exists.",
                    entity.getAuthor().getId(), entity.getAttraction().getId());
            throw new ValidationException("You have already left a review");
        } else {
            User user = userService.getById(entity.getAuthor().getId());
            Attraction attraction = attractionService.getById(entity.getAttraction().getId());
            entity.setAuthor(user);
            entity.setAttraction(attraction);
            return super.create(entity);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AttractionReview getById(final Long reviewId) {
        return super.getById(reviewId);
    }

    @Override
    public AttractionReview update(final Long reviewId, final AttractionReview entity) {
        AttractionReview review = getById(reviewId);
        if (!Objects.equals(review.getAuthor().getId(), entity.getAuthor().getId())) {
            log.warn("User with ID {} is attempting to update a review they do not own. Review ID: {}.",
                    entity.getAuthor().getId(), reviewId);
            throw new AccessDeniedException("You do not have permission to update this review.");
        }
        if (!Objects.equals(entity.getAttraction().getId(), review.getAttraction().getId())) {
            log.warn("User with ID {} is attempting to reassign a review to a different attraction. Review ID: {}.",
                    entity.getAuthor().getId(), reviewId);
            throw new ValidationException("The review cannot be reassigned to another attraction.");
        }
        if (entity.getComment() != null) {
            review.setComment(entity.getComment());
        }
        if (entity.getRating() != null) {
            review.setRating(entity.getRating());
        }
        return super.update(reviewId, review);
    }

    @Override
    public void delete(final Long reviewId) {
        super.delete(reviewId);
    }

    /**
     * Получает все отзывы для указанной достопримечательности, отсортированные по направлению сортировки.
     *
     * @param attId         Идентификатор достопримечательности
     * @param sortDirection Направление сортировки ("asc" или "desc")
     * @return Список отзывов для указанной достопримечательности
     */
    @Override
    @Transactional(readOnly = true)
    public List<AttractionReview> getReviewForAttraction(long attId, String sortDirection) {
        if (!"asc".equalsIgnoreCase(sortDirection) && !"desc".equalsIgnoreCase(sortDirection)) {
            log.warn("Invalid sortDirection value: {}. Allowed values are 'asc' or 'desc'.",
                    sortDirection);
            throw new ValidationException("Invalid value for sortDirection. Allowed values: 'asc' or 'desc'.");
        }
        return reviewRepository.findByAttractionId(attId, sortDirection.toLowerCase());
    }
}