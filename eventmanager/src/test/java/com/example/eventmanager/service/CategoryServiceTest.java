package com.example.eventmanager.service;

import com.example.eventmanager.exception.ResourceNotFoundException;
import com.example.eventmanager.model.Category;
import com.example.eventmanager.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");
    }

    @Test
    void findAll_returnsAllCategories() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        List<Category> result = categoryService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findById_existingId_returnsCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        Category result = categoryService.findById(1L);
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(99L));
    }

    @Test
    void save_validCategory_returnsSavedCategory() {
        when(categoryRepository.save(category)).thenReturn(category);
        Category result = categoryService.save(category);
        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void update_existingId_updatesCategory() {
        Category updated = new Category();
        updated.setName("Updated Category");
        updated.setDescription("Updated Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updated);
        Category result = categoryService.update(1L, updated);
        assertNotNull(result);
        assertEquals("Updated Category", result.getName());
    }

    @Test
    void delete_existingId_deletesCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        categoryService.delete(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_nonExistingId_throwsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(99L));
    }
}