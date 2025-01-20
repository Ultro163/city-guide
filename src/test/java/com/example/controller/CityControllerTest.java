package com.example.controller;

import com.example.dto.city.CityDto;
import com.example.dto.city.NewCityDto;
import com.example.dto.city.UpdateCityDto;
import com.example.dto.mappers.CityMapper;
import com.example.model.City;
import com.example.service.CrudService;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CrudService<City, Long> cityServiceImpl;

    @Mock
    private CityMapper cityMapper;

    @InjectMocks
    private CityController cityController;

    private final NewCityDto newCityDto = new NewCityDto("Париж", "Франция");
    private final CityDto cityDto = new CityDto(1L, "Париж", "Франция");
    private final City newCity = new City();

    @BeforeEach
    public void setUp() {
        newCity.setId(1L);
        newCity.setName("Париж");
        newCity.setCountry("Франция");

        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();
    }

    @Test
    public void createCityWithValidData() throws Exception {
        Mockito.when(cityMapper.toEntity(newCityDto)).thenReturn(new City());
        Mockito.when(cityMapper.toCityDto(Mockito.any(City.class))).thenReturn(cityDto);
        Mockito.when(cityServiceImpl.create(Mockito.any(City.class))).thenReturn(new City());

        mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Париж\", \"country\":\"Франция\"}"))
                .andExpect(status().isCreated());

        verify(cityServiceImpl, times(1)).create(Mockito.any(City.class));
    }

    @Test
    public void getCityByIdWithValidData() throws Exception {
        Mockito.when(cityServiceImpl.getById(1L)).thenReturn(new City());
        Mockito.when(cityMapper.toCityDto(Mockito.any(City.class))).thenReturn(cityDto);

        mockMvc.perform(get("/cities/{cityId}", 1L))
                .andExpect(status().isOk());

        verify(cityServiceImpl, times(1)).getById(1L);
    }

    @Test
    public void updateCityWithValidData() throws Exception {
        Mockito.when(cityMapper.toEntity(Mockito.any(UpdateCityDto.class))).thenReturn(newCity);
        Mockito.when(cityMapper.toCityDto(newCity)).thenReturn(cityDto);
        Mockito.when(cityServiceImpl.update(1L, newCity)).thenReturn(newCity);

        mockMvc.perform(patch("/cities/{cityId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Не Париж\", \"country\":\"Не Франция\"}"))
                .andExpect(status().isOk());

        verify(cityServiceImpl, times(1)).update(1L, newCity);
    }

    @Test
    public void deleteCityWithValidData() throws Exception {
        mockMvc.perform(delete("/cities/{cityId}", 1L))
                .andExpect(status().isNoContent());

        verify(cityServiceImpl, times(1)).delete(1L);
    }

    @Test
    public void createCityWithoutRequiredField() throws Exception {
        mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(cityServiceImpl);
    }
}
