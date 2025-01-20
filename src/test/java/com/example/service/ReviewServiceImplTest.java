package com.example.service;

import com.example.error.exception.AccessDeniedException;
import com.example.error.exception.ValidationException;
import com.example.model.Attraction;
import com.example.model.AttractionReview;
import com.example.model.User;
import com.example.repository.AttractionReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private AttractionReviewRepository reviewRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AttractionServiceImpl attractionService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private AttractionReview attractionReview;
    private Attraction attraction;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Василий");

        attraction = new Attraction();
        attraction.setId(1L);
        attraction.setName("Эйфелева башня");

        attractionReview = new AttractionReview();
        attractionReview.setId(1L);
        attractionReview.setAttraction(attraction);
        attractionReview.setAuthor(user);
        attractionReview.setRating(5);
        attractionReview.setComment("Вау!");
    }

    @Test
    public void testCreateReview() {
        when(reviewRepository.findByAttractionIdAndAuthorId(1L, 1L)).thenReturn(Optional.empty());
        when(userService.getById(1L)).thenReturn(user);
        when(attractionService.getById(1L)).thenReturn(attraction);
        when(reviewRepository.save(any(AttractionReview.class))).thenReturn(attractionReview);

        AttractionReview result = reviewService.create(attractionReview);

        assertNotNull(result);
        assertEquals("Вау!", result.getComment());
        verify(reviewRepository, times(1)).save(any(AttractionReview.class));
    }

    @Test
    public void testCreateReviewAlreadyExists() {
        when(reviewRepository.findByAttractionIdAndAuthorId(1L, 1L)).thenReturn(Optional.of(attractionReview));

        assertThrows(ValidationException.class, () -> reviewService.create(attractionReview));
    }

    @Test
    public void testGetReviewById() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(attractionReview));

        AttractionReview result = reviewService.getById(1L);

        assertNotNull(result);
        assertEquals("Вау!", result.getComment());
    }

    @Test
    public void testUpdateReview() {
        AttractionReview updatedReview = new AttractionReview();
        updatedReview.setId(1L);
        updatedReview.setAttraction(attraction);
        updatedReview.setAuthor(user);
        updatedReview.setRating(4);
        updatedReview.setComment("Не Вау!");

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(attractionReview));
        when(reviewRepository.save(any(AttractionReview.class))).thenReturn(updatedReview);

        AttractionReview result = reviewService.update(1L, updatedReview);

        assertNotNull(result);
        assertEquals("Не Вау!", result.getComment());
        assertEquals(4, result.getRating());
        verify(reviewRepository, times(1)).save(any(AttractionReview.class));
    }

    @Test
    public void testUpdateReviewAccessDenied() {
        AttractionReview updatedReview = new AttractionReview();
        updatedReview.setId(1L);
        updatedReview.setAttraction(attraction);
        updatedReview.setAuthor(new User());
        updatedReview.getAuthor().setId(2L);

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(attractionReview));

        assertThrows(AccessDeniedException.class, () -> reviewService.update(1L, updatedReview));
    }

    @Test
    public void testDeleteReview() {
        when(reviewRepository.existsById(1L)).thenReturn(true);

        reviewService.delete(1L);

        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetReviewsForAttraction() {
        when(reviewRepository.findByAttractionId(1L, "asc")).thenReturn(List.of(attractionReview));

        var result = reviewService.getReviewForAttraction(1L, "asc");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
