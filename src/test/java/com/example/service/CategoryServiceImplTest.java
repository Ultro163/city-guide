package com.example.service;

import com.example.error.exception.EntityNotFoundException;
import com.example.model.Category;
import com.example.repository.CategoryRepository;
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
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Историческое место");
    }

    @Test
    public void testCreateCategory() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.create(category);

        assertNotNull(result);
        assertEquals("Историческое место", result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testGetById() {
        when(categoryRepository.findById(1L)).thenReturn(java.util.Optional.of(category));

        Category result = categoryService.getById(1L);

        assertNotNull(result);
        assertEquals("Историческое место", result.getName());
    }

    @Test
    public void testUpdateCategory() {
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Не Историческое место");

        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        Category result = categoryService.update(1L, updatedCategory);

        assertNotNull(result);
        assertEquals("Не Историческое место", result.getName());
        assertEquals(1L, result.getId());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testDeleteCategory() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdateCategoryNotFound() {
        Category updatedCategory = new Category();
        updatedCategory.setId(555L);
        updatedCategory.setName("Не Историческое место");

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(555L, updatedCategory));
    }

    @Test
    public void testDeleteCategoryNotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> categoryService.delete(1L));
    }
}
