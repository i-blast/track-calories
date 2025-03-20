package com.pii.tracker.track_calories.meal.mapper;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;
import com.pii.tracker.track_calories.meal.model.Meal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MealMapper {

    public MealResponseDTO toDto(Meal meal) {
        return new MealResponseDTO(
                meal.getId(),
                meal.getMealTime(),
                meal.getUser().getId(),
                meal.getDishes().stream().map(Dish::getName).collect(Collectors.toList())
        );
    }

    public List<MealResponseDTO> toDtoList(List<Meal> meals) {
        return meals.stream().map(this::toDto).toList();
    }
}
