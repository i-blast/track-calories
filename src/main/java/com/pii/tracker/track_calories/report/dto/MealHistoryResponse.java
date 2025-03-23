package com.pii.tracker.track_calories.report.dto;

import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;

import java.time.LocalDate;
import java.util.List;

public record MealHistoryResponse(
        LocalDate date,

        int totalCalories,

        List<MealResponseDTO> meals
) {
}
