package com.example.service;

import com.example.error.exception.EntityNotFoundException;
import com.example.error.exception.ValidationException;
import com.example.model.Attraction;
import com.example.model.Category;
import com.example.model.City;
import com.example.model.EntityName;
import com.example.model.Location;
import com.example.repository.AttractionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса для работы с аттракционами.
 * Предоставляет операции CRUD для достопримечательностей и дополнительные функциональности,
 * такие как поиск ближайших аттракционов и аттракционов в городе.
 */
@Slf4j
@Service
@Transactional
public class AttractionServiceImpl extends AbstractCrudService<Attraction, Long> implements AttractionService {
    private final AttractionRepository attractionRepository;
    private final CategoryServiceImpl categoryService;
    private final CityServiceImpl cityService;

    @Autowired
    public AttractionServiceImpl(AttractionRepository attractionRepository, CategoryServiceImpl categoryService,
                                 CityServiceImpl cityService) {

        super(attractionRepository, EntityName.ATTRACTION);
        this.attractionRepository = attractionRepository;
        this.categoryService = categoryService;
        this.cityService = cityService;

    }

    @Override
    public Attraction create(final Attraction entity) {
        Category category = categoryService.getById(entity.getCategory().getId());
        City city = cityService.getById(entity.getCity().getId());
        entity.setCategory(category);
        entity.setCity(city);
        return super.create(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Attraction getById(final Long id) {
        log.info("Get attraction with ID={}", id);
        return attractionRepository.getAttractionById(id).orElseThrow(() -> {
            log.warn("Attraction not found with ID {}", id);
            return new EntityNotFoundException("Attraction with ID=" + id + " not found");
        });
    }

    @Override
    public Attraction update(final Long id, final Attraction entity) {
        final Attraction attraction = super.getById(id);

        if (entity.getName() != null) {
            attraction.setName(entity.getName());
        }
        if (entity.getCategory().getId() != null) {
            attraction.setCategory(entity.getCategory());
        }
        if (entity.getCity().getId() != null) {
            attraction.setCity(entity.getCity());
        }
        if (entity.getLocation() != null) {
            Location location = attraction.getLocation();
            if (entity.getLocation().getLat() != null) {
                location.setLat(entity.getLocation().getLat());
            }
            if (entity.getLocation().getLon() != null) {
                location.setLon(entity.getLocation().getLon());
            }
            attraction.setLocation(location);
        }
        return super.update(id, attraction);
    }

    @Override
    public void delete(final Long id) {
        super.delete(id);
    }

    /**
     * Получает список ближайших аттракционов в радиусе от указанной точки.
     * Также можно фильтровать по категории, минимальному рейтингу и сортировать результаты.
     *
     * @param userLat       Широта пользователя
     * @param userLon       Долгота пользователя
     * @param radius        Радиус поиска (в километрах)
     * @param categoryId    Идентификатор категории для фильтрации
     * @param minRating     Минимальный рейтинг для фильтрации
     * @param limit         Максимальное количество аттракционов для возврата
     * @param sortBy        Поле для сортировки (можно по 'distance' или 'rating')
     * @param sortDirection Направление сортировки ('asc' или 'desc')
     * @return Список ближайших аттракционов
     * @throws ValidationException Если параметры сортировки неверны
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attraction> getNearestAttractions(double userLat, double userLon, double radius,
                                                  Long categoryId, Double minRating, int limit,
                                                  String sortBy, String sortDirection) {
        log.info("Getting nearest attractions with parameters - " +
                        "User Latitude: {}, User Longitude: {}, Radius: {} km, Category ID: {}, " +
                        "Minimum Rating: {}, Limit: {}, Sort By: {}, Sort Direction: {}",
                userLat, userLon, radius, categoryId, minRating, limit, sortBy, sortDirection);
        validateSortParameters(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(0, limit, Sort.unsorted());
        return attractionRepository.getNearestAttractions(userLat, userLon, radius, categoryId,
                minRating, sortBy.toLowerCase(), sortDirection.toLowerCase(), pageable);
    }

    /**
     * Получает список аттракционов в указанном городе с учетом параметров поиска.
     * Можно фильтровать по категории, минимальному рейтингу и сортировать результаты.
     *
     * @param cityId        Идентификатор города
     * @param userLat       Широта пользователя
     * @param userLon       Долгота пользователя
     * @param categoryId    Идентификатор категории для фильтрации
     * @param minRating     Минимальный рейтинг для фильтрации
     * @param limit         Максимальное количество аттракционов для возврата
     * @param sortBy        Поле для сортировки (можно по 'distance' или 'rating')
     * @param sortDirection Направление сортировки ('asc' или 'desc')
     * @return Список аттракционов в городе
     * @throws ValidationException Если параметры сортировки неверны
     */
    @Override
    @Transactional(readOnly = true)
    public List<Attraction> getAttractionsInCity(long cityId, double userLat, double userLon,
                                                 Long categoryId, Double minRating, int limit,
                                                 String sortBy, String sortDirection) {
        log.info("Getting attractions in city with parameters - " +
                        "City ID: {}, User Latitude: {}, User Longitude: {}, Category ID: {}, " +
                        "Minimum Rating: {}, Limit: {}, Sort By: {}, Sort Direction: {}",
                cityId, userLat, userLon, categoryId, minRating, limit, sortBy, sortDirection);
        validateSortParameters(sortBy, sortDirection);
        Pageable pageable = PageRequest.of(0, limit, Sort.unsorted());
        return attractionRepository.getAttractionsInCity(cityId, userLat, userLon, categoryId,
                minRating, sortBy.toLowerCase(), sortDirection.toLowerCase(), pageable);
    }

    /**
     * Проверяет параметры сортировки.
     * Если они неверные, выбрасывает исключение {@link ValidationException}.
     *
     * @param sortBy        Поле для сортировки
     * @param sortDirection Направление сортировки
     * @throws ValidationException Если параметры сортировки неверны
     */
    private void validateSortParameters(String sortBy, String sortDirection) {
        if (!"distance".equalsIgnoreCase(sortBy) && !"rating".equalsIgnoreCase(sortBy)) {
            log.warn("Invalid value for sortBy: {}. Allowed values: 'distance' or 'rating'.", sortBy);
            throw new ValidationException("Invalid value for sortBy. Allowed values: 'distance' or 'rating'.");
        }
        if (!"asc".equalsIgnoreCase(sortDirection) && !"desc".equalsIgnoreCase(sortDirection)) {
            log.warn("Invalid value for sortDirection: {}. Allowed values: 'asc' or 'desc'.", sortDirection);
            throw new ValidationException("Invalid value for sortDirection. Allowed values: 'asc' or 'desc'.");
        }
    }

}