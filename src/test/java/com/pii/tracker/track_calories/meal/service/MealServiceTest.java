package com.pii.tracker.track_calories.meal.service;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.dish.repo.DishRepository;
import com.pii.tracker.track_calories.meal.dto.CreateMealRequestDTO;
import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;
import com.pii.tracker.track_calories.meal.exception.BadMealCreationException;
import com.pii.tracker.track_calories.meal.mapper.MealMapper;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.meal.repo.MealRepository;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestDish;
import static com.pii.tracker.track_calories.util.TestDataFactory.createTestUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {

    @Mock
    private MealRepository mealRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DishRepository dishRepository;
    @Mock
    private MealMapper mealMapper;
    @InjectMocks
    private MealService mealService;

    private User user;
    private List<Dish> dishes;
    private List<Long> dishIds;
    private CreateMealRequestDTO createMealRequestDTO;
    private Meal meal;
    private MealResponseDTO mealResponseDTO;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        user.setId(1L);
        dishIds = List.of(1L, 2L);
        dishes = dishIds.stream()
                .map(id -> {
                    Dish dish = createTestDish();
                    dish.setId(id);
                    return dish;
                })
                .toList();
        createMealRequestDTO = new CreateMealRequestDTO(1L, List.of(1L, 2L));
        meal = Meal.builder()
                .user(user)
                .dishes(dishes)
                .mealTime(LocalDateTime.now())
                .build();
        mealResponseDTO = new MealResponseDTO(1L, LocalDateTime.now(), 1L, List.of("Блюдо1", "Блюдо2"));
    }

    @Test
    void testCreateMeal_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dishRepository.findAllById(dishIds)).thenReturn(dishes);
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);
        when(mealMapper.toDto(any(Meal.class))).thenReturn(mealResponseDTO);
        var result = mealService.createMeal(createMealRequestDTO);
        assertNotNull(result);
        assertEquals(mealResponseDTO, result);
    }

    @Test
    void testCreateMeal_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            mealService.createMeal(createMealRequestDTO);
        });
        verify(userRepository, times(1)).findById(1L);
        verify(dishRepository, never()).findAllById(any());
        verify(mealRepository, never()).save(any());
    }

    @Test
    void testCreateMeal_NoDishes() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        var invalidDTO = new CreateMealRequestDTO(1L, List.of());
        assertThrows(BadMealCreationException.class, () -> {
            mealService.createMeal(invalidDTO);
        });
    }

    @Test
    void testCreateMeal_SomeDishesNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(dishRepository.findAllById(dishIds)).thenReturn(List.of(dishes.get(0)));
        assertThrows(BadMealCreationException.class, () -> {
            mealService.createMeal(createMealRequestDTO);
        });
    }

    @Test
    void testGetMealsByUser_Success() {
        when(mealRepository.findByUserId(1L)).thenReturn(List.of(meal));
        when(mealMapper.toDtoList(List.of(meal))).thenReturn(List.of(mealResponseDTO));
        List<MealResponseDTO> result = mealService.getMealsByUser(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mealResponseDTO, result.get(0));
    }

    @Test
    void testGetMealsByUser_NoMeals() {
        when(mealRepository.findByUserId(1L)).thenReturn(List.of());
        List<MealResponseDTO> result = mealService.getMealsByUser(1L);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mealRepository, times(1)).findByUserId(1L);
    }
}
