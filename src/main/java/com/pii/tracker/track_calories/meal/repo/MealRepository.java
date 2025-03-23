package com.pii.tracker.track_calories.meal.repo;

import com.pii.tracker.track_calories.meal.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUserId(Long userId);

    List<Meal> findByUserIdAndMealTimeBetween(Long userId, LocalDateTime start, LocalDateTime end);

}
