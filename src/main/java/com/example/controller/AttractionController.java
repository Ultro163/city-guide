package com.example.controller;

import com.example.dto.attraction.AttractionDto;
import com.example.dto.attraction.AttractionShortDto;
import com.example.dto.attraction.NewAttractionDto;
import com.example.dto.attraction.UpdateAttractionDto;
import com.example.dto.mappers.AttractionMapper;
import com.example.model.Attraction;
import com.example.service.AttractionService;
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
 * Контроллер для управления сущностями типа "Достопримечательность".
 * Предоставляет API для создания, обновления, удаления и получения достопримечательностей,
 * а также для поиска ближайших достопримечательностей и достопримечательностей в городе.
 */
@RestController
@RequestMapping("/attractions")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;
    private final AttractionMapper attractionMapper;

    /**
     * Создает новую достопримечательность.
     *
     * @param dto объект {@link NewAttractionDto}, содержащий данные для создания достопримечательности.
     * @return объект {@link AttractionShortDto}, представляющий краткую информацию о созданной достопримечательности.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AttractionShortDto createAttraction(@RequestBody @Valid NewAttractionDto dto) {
        Attraction attraction = attractionMapper.toEntity(dto);
        return attractionMapper.toAttractionShortDto(attractionService.create(attraction));
    }

    /**
     * Возвращает информацию о достопримечательности по её идентификатору.
     *
     * @param attId идентификатор достопримечательности.
     * @return объект {@link AttractionDto}, содержащий полную информацию и рейтинг достопримечательности.
     */
    @GetMapping("/{attId}")
    public AttractionDto getAttractionById(@PathVariable long attId) {
        return attractionMapper.toAttractionDto(attractionService.getById(attId));
    }

    /**
     * Обновляет информацию о достопримечательности.
     *
     * @param attId идентификатор достопримечательности.
     * @param dto   объект {@link UpdateAttractionDto}, содержащий обновлённые данные.
     * @return объект {@link AttractionShortDto}, представляющий краткую информацию об обновлённой достопримечательности.
     */
    @PatchMapping("/{attId}")
    public AttractionShortDto updateAttraction(@PathVariable long attId, @RequestBody UpdateAttractionDto dto) {
        Attraction attraction = attractionMapper.toEntity(dto);
        return attractionMapper.toAttractionShortDto(attractionService.update(attId, attraction));
    }

    /**
     * Удаляет достопримечательность по её идентификатору.
     *
     * @param attId идентификатор достопримечательности.
     */
    @DeleteMapping("/{attId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAttraction(@PathVariable Long attId) {
        attractionService.delete(attId);
    }

    /**
     * Возвращает список ближайших достопримечательностей в радиусе от заданной точки.
     *
     * @param userLat       широта пользователя.
     * @param userLon       долгота пользователя.
     * @param radius        радиус поиска в километрах.
     * @param categoryId    (необязательно) идентификатор категории достопримечательностей.
     * @param minRating     (необязательно) минимальный рейтинг достопримечательности.
     * @param limitCount    (по умолчанию 10) максимальное количество достопримечательностей в ответе.
     * @param sortBy        (по умолчанию "distance") критерий сортировки: "distance", "rating".
     * @param sortDirection (по умолчанию "asc") направление сортировки: "asc" или "desc".
     * @return список объектов {@link AttractionDto}, представляющих ближайшие достопримечательности.
     */
    @GetMapping("/nearby")
    public List<AttractionDto> getNearestAttractions(@RequestParam double userLat,
                                                     @RequestParam double userLon,
                                                     @RequestParam double radius,
                                                     @RequestParam(required = false) Long categoryId,
                                                     @RequestParam(required = false) Double minRating,
                                                     @RequestParam(defaultValue = "10") int limitCount,
                                                     @RequestParam(defaultValue = "distance") String sortBy,
                                                     @RequestParam(defaultValue = "asc") String sortDirection) {
        return attractionService.getNearestAttractions(userLat, userLon, radius,
                        categoryId, minRating, limitCount, sortBy, sortDirection).stream()
                .map(attractionMapper::toAttractionDto).toList();
    }

    /**
     * Возвращает список достопримечательностей в заданном городе.
     *
     * @param cityId        идентификатор города.
     * @param userLat       широта пользователя.
     * @param userLon       долгота пользователя.
     * @param categoryId    (необязательно) идентификатор категории достопримечательностей.
     * @param minRating     (необязательно) минимальный рейтинг достопримечательности.
     * @param limitCount    (по умолчанию 10) максимальное количество достопримечательностей в ответе.
     * @param sortBy        (по умолчанию "distance") критерий сортировки: "distance", "rating".
     * @param sortDirection (по умолчанию "asc") направление сортировки: "asc" или "desc".
     * @return список объектов {@link AttractionDto}, представляющих достопримечательности в городе.
     */
    @GetMapping("/city")
    public List<AttractionDto> getAttractionsInCity(@RequestParam long cityId,
                                                    @RequestParam double userLat,
                                                    @RequestParam double userLon,
                                                    @RequestParam(required = false) Long categoryId,
                                                    @RequestParam(required = false) Double minRating,
                                                    @RequestParam(defaultValue = "10") int limitCount,
                                                    @RequestParam(defaultValue = "distance") String sortBy,
                                                    @RequestParam(defaultValue = "asc") String sortDirection) {
        return attractionService.getAttractionsInCity(cityId, userLat, userLon,
                        categoryId, minRating, limitCount, sortBy, sortDirection).stream()
                .map(attractionMapper::toAttractionDto).toList();
    }
}