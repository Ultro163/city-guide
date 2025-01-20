package com.example.controller;

import com.example.dto.category.CategoryDto;
import com.example.dto.category.NewCategoryDto;
import com.example.dto.mappers.CategoryMapper;
import com.example.model.Category;
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
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CrudService<Category, Long> categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private final NewCategoryDto newCategoryDto = new NewCategoryDto("Историческое место");
    private final CategoryDto categoryDto = new CategoryDto(1L, "Историческое место");
    private final Category newCategory = new Category();

    @BeforeEach
    public void setUp() {
        newCategory.setId(1L);
        newCategory.setName("Историческое место");

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void createCategoryWithValidData() throws Exception {
        Mockito.when(categoryMapper.toEntity(newCategoryDto)).thenReturn(newCategory);
        Mockito.when(categoryMapper.toCategoryDto(Mockito.any(Category.class))).thenReturn(categoryDto);
        Mockito.when(categoryService.create(Mockito.any(Category.class))).thenReturn(newCategory);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Историческое место\"}"))
                .andExpect(status().isCreated());

        verify(categoryService, times(1)).create(Mockito.any(Category.class));
    }

    @Test
    public void getCategoryByIdWithValidData() throws Exception {
        Mockito.when(categoryService.getById(1L)).thenReturn(newCategory);
        Mockito.when(categoryMapper.toCategoryDto(Mockito.any(Category.class))).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/{catId}", 1L))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).getById(1L);
    }

    @Test
    public void updateCategoryWithValidData() throws Exception {
        Mockito.when(categoryMapper.toEntity(Mockito.any(NewCategoryDto.class))).thenReturn(newCategory);
        Mockito.when(categoryMapper.toCategoryDto(newCategory)).thenReturn(categoryDto);
        Mockito.when(categoryService.update(1L, newCategory)).thenReturn(newCategory);

        mockMvc.perform(patch("/categories/{catId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Не Историческое место\"}"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).update(1L, newCategory);
    }

    @Test
    public void deleteCategoryWithValidData() throws Exception {
        mockMvc.perform(delete("/categories/{catId}", 1L))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).delete(1L);
    }

    @Test
    public void createCategoryWithoutRequiredField() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Очень большое название,Очень большое название,Очень большое название," +
                                "Очень большое название,Очень большое название,Очень большое название," +
                                "Очень большое название,Очень большое название,Очень большое название," +
                                "Очень большое название,Очень большое название,Очень большое название," +
                                "Очень большое название,Очень большое название,Очень большое название\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(categoryService);
    }
}