package com.pii.tracker.track_calories.report.service;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.meal.service.MealService;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.pii.tracker.track_calories.util.DateTimeUtils.endOfDay;
import static com.pii.tracker.track_calories.util.DateTimeUtils.startOfDay;
import static com.pii.tracker.track_calories.util.TestDataFactory.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private MealService mealService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ReportService reportService;

    private User user;
    private Dish testDish;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        user.setId(1L);
        user.setDailyCalorieIntake(2000);

        testDish = createTestDish();
        testDish.setId(1L);
    }

    @Test
    void getDailyReport_ShouldReturnCorrectResponse() {
        var date = LocalDate.now();
        var meal = createTestMeal(user, List.of(testDish), LocalDateTime.now());
        when(mealService.getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date))).thenReturn(List.of(meal));
        when(userService.getUserById(1L)).thenReturn(user);

        var response = reportService.getDailyReport(1L, date);
        assertEquals(1, response.meals().size());
        assertEquals(250, response.totalCalories());
        verify(mealService, times(1)).getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getDailyReport_WhenNoMeals_ShouldReturnZeroCalories() {
        var date = LocalDate.now();
        when(mealService.getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date))).thenReturn(List.of());
        when(userService.getUserById(1L)).thenReturn(user);

        var response = reportService.getDailyReport(1L, date);
        assertEquals(0, response.totalCalories());
        assertEquals(0, response.meals().size());
        verify(mealService, times(1)).getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getDailyReport_WhenUserNotFound_ShouldThrowException() {
        var date = LocalDate.now();
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("Пользователь не найден id=1"));;;

        var exception = assertThrows(UserNotFoundException.class,
                () -> reportService.getDailyReport(1L, date));
        assertEquals("Пользователь не найден id=1", exception.getMessage());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void checkCalorieLimit_ShouldReturnTrue_WhenCaloriesWithinLimit() {
        var meal = createTestMeal(user, List.of(testDish), LocalDateTime.now());
        var date = LocalDate.now();
        when(userService.getUserById(1L)).thenReturn(user);
        when(mealService.getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date)))
                .thenReturn(List.of(meal));

        var response = reportService.checkCalorieLimit(1L, date);
        assertTrue(response.isWithinLimit());
    }

    @Test
    void checkCalorieLimit_ShouldReturnFalse_WhenCaloriesExceedLimit() {
        var highCalorieDish = Dish.builder()
                .name("high calorie dish")
                .calories(2500)
                .build();
        var meal = createTestMeal(user, List.of(highCalorieDish), LocalDateTime.now());
        var date = LocalDate.now();
        when(userService.getUserById(1L)).thenReturn(user);
        when(mealService.getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date)))
                .thenReturn(List.of(meal));

        var response = reportService.checkCalorieLimit(1L, date);
        assertFalse(response.isWithinLimit());
    }

    @Test
    void checkCalorieLimit_WhenUserNotFound_ShouldThrowException() {
        var date = LocalDate.now();
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("Пользователь не найден id=1"));
        var exception = assertThrows(UserNotFoundException.class,
                () -> reportService.checkCalorieLimit(1L, date));
        assertEquals("Пользователь не найден id=1", exception.getMessage());
    }

    @Test
    void getMealHistory_ShouldReturnCorrectHistory() {
        var meal = createTestMeal(user, List.of(testDish), LocalDateTime.now().minusDays(2L));
        var start = LocalDate.now().minusDays(7);
        var end = LocalDate.now();
        when(userService.getUserById(1L)).thenReturn(user);
        when(mealService.getMealsByUserAndDateRange(eq(1L), any(), any())).thenReturn(List.of(meal));

        var history = reportService.getMealHistory(1L, start, end);
        assertEquals(8, history.size());
        assertEquals(250, history.get(0).totalCalories());
    }

    @Test
    void getMealHistory_WhenUserNotFound_ShouldThrowException() {
        var start = LocalDate.now().minusDays(7);
        var end = LocalDate.now();
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("Пользователь не найден id=1"));
        var exception = assertThrows(UserNotFoundException.class,
                () -> reportService.getMealHistory(1L, start, end));
        assertEquals("Пользователь не найден id=1", exception.getMessage());
    }

    @Test
    void getMealHistory_WhenInvalidDateRange_ShouldThrowException() {
        var start = LocalDate.now();
        var end = LocalDate.now().minusDays(1);
        var exception = assertThrows(IllegalArgumentException.class,
                () -> reportService.getMealHistory(1L, start, end));
        assertEquals("Некорректный диапазон дат", exception.getMessage());
    }
}