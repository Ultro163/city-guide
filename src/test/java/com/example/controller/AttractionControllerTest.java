package com.example.controller;

import com.example.dto.attraction.AttractionDto;
import com.example.dto.attraction.AttractionShortDto;
import com.example.dto.attraction.NewAttractionDto;
import com.example.dto.attraction.UpdateAttractionDto;
import com.example.dto.location.LocationDto;
import com.example.dto.mappers.AttractionMapper;
import com.example.model.Attraction;
import com.example.service.AttractionService;
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
class AttractionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AttractionMapper attractionMapper;

    @Mock
    private AttractionService attractionService;

    @InjectMocks
    private AttractionController attractionController;

    private Attraction attraction;
    private AttractionDto attractionDto;
    private AttractionShortDto attractionShortDto;
    private NewAttractionDto newAttractionDto;
    private UpdateAttractionDto updateAttractionDto;

    @BeforeEach
    void setUp() {
        attraction = new Attraction();
        attraction.setId(1L);

        attractionDto = new AttractionDto();
        attractionDto.setId(1L);

        attractionShortDto = new AttractionShortDto();
        attractionShortDto.setId(1L);

        LocationDto location = new LocationDto(48.8606, 2.3376);
        newAttractionDto = new NewAttractionDto("Лувр", 3L, location, 1L);
        updateAttractionDto = new UpdateAttractionDto("Не Лувр", 3L, location, 1L);

        mockMvc = MockMvcBuilders.standaloneSetup(attractionController).build();
    }

    @Test
    void createAttraction_WithValidData_ReturnsCreated() throws Exception {
        Mockito.when(attractionMapper.toEntity(newAttractionDto)).thenReturn(attraction);
        Mockito.when(attractionMapper.toAttractionShortDto(Mockito.any(Attraction.class)))
                .thenReturn(attractionShortDto);
        Mockito.when(attractionService.create(Mockito.any(Attraction.class))).thenReturn(attraction);

        mockMvc.perform(post("/attractions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Лувр\", \"categoryId\":3,\"location\":{\"lat\":48.8606,\"lon\":2.3376}," +
                                "\"cityId\":1}"))
                .andExpect(status().isCreated());

        verify(attractionService, times(1)).create(Mockito.any(Attraction.class));
    }

    @Test
    void getAttractionById_WithValidId_ReturnsOk() throws Exception {
        Mockito.when(attractionService.getById(1L)).thenReturn(attraction);
        Mockito.when(attractionMapper.toAttractionDto(attraction)).thenReturn(attractionDto);

        mockMvc.perform(get("/attractions/{attId}", 1L))
                .andExpect(status().isOk());

        verify(attractionService, times(1)).getById(1L);
    }

    @Test
    void updateAttraction_WithValidData_ReturnsOk() throws Exception {
        Mockito.when(attractionMapper.toEntity(updateAttractionDto)).thenReturn(attraction);
        Mockito.when(attractionMapper.toAttractionShortDto(Mockito.any(Attraction.class)))
                .thenReturn(attractionShortDto);
        Mockito.when(attractionService.update(1L, attraction)).thenReturn(attraction);

        mockMvc.perform(patch("/attractions/{attId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Не Лувр\", \"categoryId\":3," +
                                "\"location\":{\"lat\":48.8606,\"lon\":2.3376},\"cityId\":1}"))
                .andExpect(status().isOk());

        verify(attractionService, times(1)).update(1L, attraction);
    }

    @Test
    void deleteAttraction_WithValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/attractions/{attId}", 1L))
                .andExpect(status().isNoContent());

        verify(attractionService, times(1)).delete(1L);
    }

    @Test
    void getNearestAttractions_WithValidParameters_ReturnsOk() throws Exception {
        Mockito.when(attractionService.getNearestAttractions(Mockito.anyDouble(), Mockito.anyDouble(),
                Mockito.anyDouble(), Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyInt(),
                Mockito.anyString(), Mockito.anyString())).thenReturn(List.of(attraction));
        Mockito.when(attractionMapper.toAttractionDto(Mockito.any(Attraction.class))).thenReturn(attractionDto);

        mockMvc.perform(get("/attractions/nearby")
                        .param("userLat", "40.7128")
                        .param("userLon", "-74.0060")
                        .param("radius", "10.0")
                        .param("categoryId", "1")
                        .param("minRating", "3.3")
                        .param("limitCount", "10")
                        .param("sortBy", "distance")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk());

        verify(attractionService, times(1)).getNearestAttractions(40.7128,
                -74.0060, 10.0, 1L, 3.3, 10, "distance", "asc");
    }

    @Test
    void getAttractionsInCity_WithValidParameters_ReturnsOk() throws Exception {
        Mockito.when(attractionService.getAttractionsInCity(Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyDouble(),
                Mockito.anyLong(), Mockito.anyDouble(), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyString())).thenReturn(List.of(attraction));
        Mockito.when(attractionMapper.toAttractionDto(Mockito.any(Attraction.class))).thenReturn(attractionDto);

        mockMvc.perform(get("/attractions/city")
                        .param("cityId", "1")
                        .param("userLat", "40.7128")
                        .param("userLon", "-74.0060")
                        .param("categoryId", "1")
                        .param("minRating", "3.3")
                        .param("limitCount", "10")
                        .param("sortBy", "distance")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk());

        verify(attractionService, times(1)).getAttractionsInCity(1L, 40.7128,
                -74.0060, 1L, 3.3, 10, "distance", "asc");
    }
}
