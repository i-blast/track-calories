package com.pii.tracker.track_calories.meal.repo;

import com.pii.tracker.track_calories.meal.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MealRepository extends JpaRepository<Meal, Long> {

    Set<Meal> findByUserId(Long userId);

}
