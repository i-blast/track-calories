package com.pii.tracker.track_calories.dish.repo;

import com.pii.tracker.track_calories.dish.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

}
