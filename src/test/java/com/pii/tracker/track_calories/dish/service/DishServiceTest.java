package com.pii.tracker.track_calories.dish.service;

import com.pii.tracker.track_calories.dish.exception.DishNotFoundException;
import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.dish.repo.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestDish;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = createTestDish();
    }

    @Test
    void testCreateDish() {
        when(dishRepository.save(dish)).thenReturn(dish);

        var createdDish = dishService.createDish(dish);

        assertNotNull(createdDish);
        assertEquals(dish.getName(), createdDish.getName());
        assertEquals(dish.getCalories(), createdDish.getCalories());
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void testGetAllDishes() {
        when(dishRepository.findAll()).thenReturn(List.of(dish));

        var dishes = dishService.getAllDishes();

        assertNotNull(dishes);
        assertEquals(1, dishes.size());
        assertEquals(dish.getName(), dishes.get(0).getName());
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    void testGetDishById() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        var foundDish = dishService.getDishById(1L);

        assertTrue(foundDish.isPresent());
        assertEquals(dish.getName(), foundDish.get().getName());
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDishById_NotFound() {
        when(dishRepository.findById(2L)).thenReturn(Optional.empty());

        var foundDish = dishService.getDishById(2L);

        assertFalse(foundDish.isPresent());
        verify(dishRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateDish() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishRepository.save(dish)).thenReturn(dish);

        var updatedDish = Dish.builder()
                .name("updated")
                .calories(600)
                .proteins(40)
                .fats(30)
                .carbohydrates(60)
                .build();

        var result = dishService.updateDish(1L, updatedDish);

        assertNotNull(result);
        assertEquals(updatedDish.getName(), result.getName());
        assertEquals(updatedDish.getCalories(), result.getCalories());
        verify(dishRepository, times(1)).findById(1L);
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    void testDeleteDish() {
        doNothing().when(dishRepository).deleteById(1L);
        dishService.deleteDish(1L);
        verify(dishRepository, times(1)).deleteById(1L);
    }
}
