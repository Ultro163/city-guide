package com.example.service;

import com.example.model.Category;
import com.example.model.EntityName;
import com.example.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Реализация сервиса для работы с категориями.
 * Предоставляет операции CRUD для категорий.
 */
@Slf4j
@Service
@Transactional
public class CategoryServiceImpl extends AbstractCrudService<Category, Long> {

    @Autowired
    protected CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository, EntityName.CATEGORY);
    }

    @Override
    public Category create(final Category category) {
        return super.create(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(final Long catId) {
        return super.getById(catId);
    }

    @Override
    public Category update(final Long catId, final Category category) {
        super.validateExistence(catId);
        return super.update(catId, category);
    }

    @Override
    public void delete(final Long catId) {
        super.delete(catId);
    }
}