package com.example.controller;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.NewCategoryDto;
import com.example.dto.mappers.CategoryMapper;
import com.example.model.Category;
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
 * Контроллер для управления категориями.
 * Предоставляет API для создания, обновления, удаления и получения категорий.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CrudService<Category, Long> categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * Создает новую категорию.
     *
     * @param dto объект {@link NewCategoryDto}, содержащий данные для создания категории.
     * @return объект {@link CategoryDto}, представляющий полную информацию о созданной категории.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody final NewCategoryDto dto) {
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toCategoryDto(categoryService.create(category));
    }

    /**
     * Возвращает информацию о категории по её идентификатору.
     *
     * @param catId идентификатор категории.
     * @return объект {@link CategoryDto}, содержащий полную информацию о категории.
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategoryById(@PathVariable final long catId) {
        return categoryMapper.toCategoryDto(categoryService.getById(catId));
    }

    /**
     * Обновляет информацию о категории.
     *
     * @param catId идентификатор категории.
     * @param dto   объект {@link NewCategoryDto}, содержащий обновленные данные категории.
     * @return объект {@link CategoryDto}, представляющий полную информацию об обновленной категории.
     */
    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable final long catId, @Valid @RequestBody final NewCategoryDto dto) {
        Category category = categoryMapper.toEntity(dto);
        category.setId(catId);
        return categoryMapper.toCategoryDto(categoryService.update(catId, category));
    }

    /**
     * Удаляет категорию по её идентификатору.
     *
     * @param catId идентификатор категории.
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable final long catId) {
        categoryService.delete(catId);
    }
}