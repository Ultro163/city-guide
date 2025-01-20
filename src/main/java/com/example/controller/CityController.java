package com.example.controller;

import com.example.dto.city.CityDto;
import com.example.dto.city.NewCityDto;
import com.example.dto.city.UpdateCityDto;
import com.example.dto.mappers.CityMapper;
import com.example.model.City;
import com.example.service.CrudService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления городами.
 * Предоставляет API для создания, обновления, удаления и получения информации о городах.
 */
@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {
    private final CrudService<City, Long> cityService;
    private final CityMapper cityMapper;

    /**
     * Создает новый город.
     *
     * @param dto объект {@link NewCityDto}, содержащий данные для создания города.
     * @return объект {@link CityDto}, представляющий полную информацию о созданном городе.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto createCity(@RequestBody @Valid final NewCityDto dto) {
        City city = cityMapper.toEntity(dto);
        return cityMapper.toCityDto(cityService.create(city));
    }

    /**
     * Возвращает информацию о городе по его идентификатору.
     *
     * @param cityId идентификатор города.
     * @return объект {@link CityDto}, содержащий полную информацию о городе.
     */
    @GetMapping("/{cityId}")
    public CityDto getCityById(@PathVariable final long cityId) {
        return cityMapper.toCityDto(cityService.getById(cityId));
    }

    /**
     * Обновляет информацию о городе.
     *
     * @param cityId идентификатор города.
     * @param dto    объект {@link UpdateCityDto}, содержащий обновленные данные города.
     * @return объект {@link CityDto}, представляющий полную информацию об обновленном городе.
     */
    @PatchMapping("/{cityId}")
    public CityDto updateCity(@PathVariable final long cityId, @RequestBody final UpdateCityDto dto) {
        City city = cityMapper.toEntity(dto);
        return cityMapper.toCityDto(cityService.update(cityId, city));
    }

    /**
     * Удаляет город по его идентификатору.
     *
     * @param cityId идентификатор города.
     */
    @DeleteMapping("/{cityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCity(@PathVariable final long cityId) {
        cityService.delete(cityId);
    }
}