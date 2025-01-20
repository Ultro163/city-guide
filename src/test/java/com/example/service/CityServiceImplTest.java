package com.example.service;

import com.example.model.City;
import com.example.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private City city;

    @BeforeEach
    public void setUp() {
        city = new City();
        city.setId(1L);
        city.setName("Париж");
        city.setCountry("Франция");
    }

    @Test
    public void testCreateCity() {
        when(cityRepository.save(any(City.class))).thenReturn(city);

        City result = cityService.create(city);

        assertNotNull(result);
        assertEquals("Париж", result.getName());
        assertEquals("Франция", result.getCountry());
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    public void testGetById() {
        when(cityRepository.findById(1L)).thenReturn(java.util.Optional.of(city));

        City result = cityService.getById(1L);

        assertNotNull(result);
        assertEquals("Париж", result.getName());
        assertEquals("Франция", result.getCountry());
    }

    @Test
    public void testUpdateCity() {
        City updatedCity = new City();
        updatedCity.setName("Не Париж");
        updatedCity.setCountry("Франция");

        when(cityRepository.findById(1L)).thenReturn(java.util.Optional.of(city));
        when(cityRepository.save(any(City.class))).thenReturn(updatedCity);

        City result = cityService.update(1L, updatedCity);

        assertNotNull(result);
        assertEquals("Не Париж", result.getName());
        assertEquals("Франция", result.getCountry());
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @Test
    public void testDeleteCity() {
        when(cityRepository.existsById(1L)).thenReturn(true);

        cityService.delete(1L);

        verify(cityRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateCityNotFound() {
        City updatedCity = new City();
        updatedCity.setName("Париж");
        updatedCity.setCountry("Франция");

        when(cityRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> cityService.update(1L, updatedCity));
    }

    @Test
    public void testDeleteCityNotFound() {
        when(cityRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> cityService.delete(1L));
    }
}
