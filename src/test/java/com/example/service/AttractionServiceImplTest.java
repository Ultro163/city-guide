package com.example.service;

import com.example.model.Attraction;
import com.example.model.Category;
import com.example.model.City;
import com.example.model.Location;
import com.example.repository.AttractionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttractionServiceImplTest {

    @Mock
    private AttractionRepository attractionRepository;

    @Mock
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private CityServiceImpl cityServiceImpl;

    @InjectMocks
    private AttractionServiceImpl attractionService;

    private Attraction attraction;
    private Category category;
    private City city;
    private Location location;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Архитектурное сооружение");

        city = new City();
        city.setId(1L);
        city.setName("Париж");
        city.setCountry("Франция");

        location = new Location();
        location.setLat(40.7128);
        location.setLon(-74.0060);

        attraction = new Attraction();
        attraction.setId(1L);
        attraction.setName("Эйфелева башня");
        attraction.setCategory(category);
        attraction.setCity(city);
        attraction.setLocation(location);
    }


    @Test
    public void testCreateAttraction() {
        when(categoryServiceImpl.getById(1L)).thenReturn(category);
        when(cityServiceImpl.getById(1L)).thenReturn(city);
        when(attractionRepository.save(any(Attraction.class))).thenReturn(attraction);

        Attraction result = attractionService.create(attraction);

        assertNotNull(result);
        assertEquals("Эйфелева башня", result.getName());
        assertEquals("Архитектурное сооружение", result.getCategory().getName());
        assertEquals("Париж", result.getCity().getName());
        verify(attractionRepository, times(1)).save(any(Attraction.class));
    }

    @Test
    public void testGetById() {
        when(attractionRepository.getAttractionById(1L)).thenReturn(Optional.of(attraction));

        Attraction result = attractionService.getById(1L);

        assertNotNull(result);
        assertEquals("Эйфелева башня", result.getName());
    }

    @Test
    public void testUpdateAttraction() {
        Attraction updatedAttraction = new Attraction();
        updatedAttraction.setName("Другая башня");
        updatedAttraction.setCategory(category);
        updatedAttraction.setCity(city);
        updatedAttraction.setLocation(location);

        when(attractionRepository.findById(1L)).thenReturn(Optional.of(attraction));
        when(attractionRepository.save(any(Attraction.class))).thenReturn(updatedAttraction);

        Attraction result = attractionService.update(1L, updatedAttraction);

        assertNotNull(result);
        assertEquals("Другая башня", result.getName());
        verify(attractionRepository, times(1)).save(any(Attraction.class));
    }

    @Test
    public void testDeleteAttraction() {
        when(attractionRepository.existsById(1L)).thenReturn(true);

        attractionService.delete(1L);

        verify(attractionRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetNearestAttractions() {
        Pageable pageable = PageRequest.of(0, 10);
        when(attractionRepository.getNearestAttractions(
                anyDouble(), anyDouble(), anyDouble(), anyLong(), anyDouble(), anyString(), anyString(), eq(pageable)))
                .thenReturn(List.of(attraction));

        var result = attractionService.getNearestAttractions(40.7128, -74.0060, 10.0,
                1L, 4.0, 10, "distance", "asc");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testGetAttractionsInCity() {
        Pageable pageable = PageRequest.of(0, 10);
        when(attractionRepository.getAttractionsInCity(anyLong(), anyDouble(), anyDouble(), anyLong(),
                anyDouble(), anyString(), anyString(), eq(pageable)))
                .thenReturn(List.of(attraction));

        var result = attractionService.getAttractionsInCity(1L, 40.7128, -74.0060,
                1L, 4.0, 10, "distance", "asc");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
