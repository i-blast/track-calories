package com.pii.tracker.track_calories.report.service;

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
import static org.junit.Assert.assertThrows;
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
    private Meal meal;

    @BeforeEach
    void setUp() {
        user = createTestUser();
        user.setId(1L);
        user.setDailyCalorieIntake(2000);

        var testDish = createTestDish();
        testDish.setId(1L);
        meal = createTestMeal(user, List.of(testDish), LocalDateTime.now());
    }

    @Test
    void getDailyReport_ShouldReturnCorrectResponse() {
        var date = LocalDate.now();
        var meals = List.of(meal);
        when(mealService.getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date))).thenReturn(meals);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

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
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        var response = reportService.getDailyReport(1L, date);
        assertEquals(0, response.totalCalories());
        assertEquals(0, response.meals().size());

        verify(mealService, times(1)).getMealsByUserAndDateRange(1L, startOfDay(date), endOfDay(date));
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getDailyReport_WhenUserNotFound_ShouldThrowException() {
        var date = LocalDate.now();
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        var exception = assertThrows(UserNotFoundException.class,
                () -> reportService.getDailyReport(1L, date));

        assertEquals("Пользователь не найден id=1", exception.getMessage());
        verify(userService, times(1)).getUserById(1L);
    }
}