package com.pii.tracker.track_calories.util;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public final class TestDataFactory {

    public static User createTestUser() {
        return User.builder()
                .name("vasYa")
                .email("vasYa@ya.ru")
                .weight(80)
                .height(175)
                .age(35)
                .weightGoal(WeightGoal.LOSE)
                .build();
    }

    public static Dish createTestDish() {
        return Dish.builder()
                .name("Салат Цезарь с курицей")
                .calories(250)
                .proteins(15)
                .fats(18)
                .carbohydrates(8)
                .build();
    }

    public static Meal createTestMeal(
            User user,
            Collection<Dish> dishes,
            LocalDateTime mealTime
    ) {
        return Meal.builder()
                .user(user)
                .dishes(List.copyOf(dishes))
                .mealTime(mealTime)
                .build();
    }

}
