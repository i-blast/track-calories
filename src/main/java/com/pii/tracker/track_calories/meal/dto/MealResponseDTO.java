package com.pii.tracker.track_calories.meal.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MealResponseDTO(
        Long id,
        LocalDateTime mealTime,
        Long userId,
        List<String> dishNames
) {
}
