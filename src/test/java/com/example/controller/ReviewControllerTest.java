package com.example.controller;

import com.example.dto.mappers.AttractionReviewMapper;
import com.example.dto.review.AttractionReviewDto;
import com.example.dto.review.NewAttractionReviewDto;
import com.example.dto.review.ReviewDtoForAttraction;
import com.example.dto.review.UpdateAttractionReviewDto;
import com.example.model.Attraction;
import com.example.model.AttractionReview;
import com.example.model.User;
import com.example.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AttractionReviewMapper attractionReviewMapper;

    @Mock
    private ReviewService reviewServiceImpl;

    @InjectMocks
    private ReviewController reviewController;

    private AttractionReview review;
    private AttractionReviewDto reviewDto;
    private NewAttractionReviewDto newReviewDto;
    private UpdateAttractionReviewDto updateReviewDto;

    @BeforeEach
    void setUp() {
        Attraction attraction = new Attraction();
        attraction.setId(1L);
        User user = new User();
        user.setId(1L);

        review = new AttractionReview();
        review.setId(1L);
        review.setAttraction(attraction);
        review.setAuthor(user);

        reviewDto = new AttractionReviewDto();
        reviewDto.setId(1L);

        newReviewDto = new NewAttractionReviewDto("ВАУ!", 5, 1L, 1L);

        updateReviewDto = new UpdateAttractionReviewDto("НЕ ВАУ!", 1, 2L, 1L);

        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void createReview_WithValidData_ReturnsCreated() throws Exception {
        Mockito.when(attractionReviewMapper.toEntity(newReviewDto)).thenReturn(review);
        Mockito.when(attractionReviewMapper.toAttractionReviewDto(Mockito.any(AttractionReview.class)))
                .thenReturn(reviewDto);
        Mockito.when(reviewServiceImpl.create(Mockito.any(AttractionReview.class))).thenReturn(review);

        mockMvc.perform(post("/reviews/user/{userId}/attraction/{attId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"ВАУ!\", \"rating\":5,\"authorId\":1," +
                                " \"attractionId\":1}"))
                .andExpect(status().isCreated());

        verify(reviewServiceImpl, times(1)).create(Mockito.any(AttractionReview.class));
    }

    @Test
    void getReviewById_WithValidId_ReturnsOk() throws Exception {
        Mockito.when(reviewServiceImpl.getById(1L)).thenReturn(review);
        Mockito.when(attractionReviewMapper.toAttractionReviewDto(review)).thenReturn(reviewDto);

        mockMvc.perform(get("/reviews/{reviewId}", 1L))
                .andExpect(status().isOk());

        verify(reviewServiceImpl, times(1)).getById(1L);
    }

    @Test
    void updateReview_WithValidData_ReturnsOk() throws Exception {
        Mockito.when(attractionReviewMapper.toEntity(updateReviewDto)).thenReturn(review);
        Mockito.when(attractionReviewMapper.toAttractionReviewDto(Mockito.any(AttractionReview.class)))
                .thenReturn(reviewDto);
        Mockito.when(reviewServiceImpl.update(1L, review)).thenReturn(review);

        mockMvc.perform(patch("/reviews/{reviewId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"НЕ ВАУ!\", \"rating\":1,\"authorId\":2," +
                                " \"attractionId\":1}"))
                .andExpect(status().isOk());

        verify(reviewServiceImpl, times(1)).update(1L, review);
    }

    @Test
    void deleteReview_WithValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/reviews/{reviewId}", 1L))
                .andExpect(status().isNoContent());

        verify(reviewServiceImpl, times(1)).delete(1L);
    }

    @Test
    void getReviewsByAttractionId_WithValidId_ReturnsOk() throws Exception {
        List<ReviewDtoForAttraction> reviewList = List.of(new ReviewDtoForAttraction(), new ReviewDtoForAttraction());
        Mockito.when(reviewServiceImpl.getReviewForAttraction(1L, "desc")).thenReturn(List.of(review));
        Mockito.when(attractionReviewMapper.toReviewDtoForAttraction(Mockito.any(AttractionReview.class)))
                .thenReturn(reviewList.get(0));

        mockMvc.perform(get("/reviews/attraction/{attId}", 1L)
                        .param("sortDirection", "desc"))
                .andExpect(status().isOk());

        verify(reviewServiceImpl, times(1)).getReviewForAttraction(1L, "desc");
    }
}
