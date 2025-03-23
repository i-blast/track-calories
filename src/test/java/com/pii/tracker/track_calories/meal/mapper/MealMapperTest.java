package com.pii.tracker.track_calories.meal.mapper;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.pii.tracker.track_calories.util.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MealMapperTest {

    private final MealMapper mealMapper = new MealMapper(); // Создаем маппер вручную

    @Test
    void toDto_ShouldMapMealToDto() {
        var user = createTestUser();
        var dish1 = createTestDish();
        var dish2 =  createTestDish();
        var meal = createTestMeal(user, List.of(dish1, dish2), LocalDateTime.now());

        var dto = mealMapper.toDto(meal);
        assertEquals(meal.getMealTime(), dto.mealTime());
        assertEquals(List.of("Салат Цезарь с курицей", "Салат Цезарь с курицей"), dto.dishNames());
    }

    @Test
    void toDtoList_ShouldMapMealsToDtoList() {
        var user = createTestUser();
        var dish1 = createTestDish();
        var dish2 =  createTestDish();
        var meal1 = createTestMeal(user, List.of(dish1, dish2), LocalDateTime.now());
        var meal2 = createTestMeal(user, List.of(dish1), LocalDateTime.now());
        var dtos = mealMapper.toDtoList(List.of(meal1, meal2));

        assertEquals(2, dtos.size());
        assertEquals(meal1.getMealTime(), dtos.get(0).mealTime());
        assertEquals(List.of("Салат Цезарь с курицей", "Салат Цезарь с курицей"), dtos.get(0).dishNames());
        assertEquals(meal2.getMealTime(), dtos.get(1).mealTime());
        assertEquals(List.of("Салат Цезарь с курицей"), dtos.get(1).dishNames());
    }
}
