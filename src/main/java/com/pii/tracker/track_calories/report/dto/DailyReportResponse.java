package com.pii.tracker.track_calories.report.dto;

import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;

import java.util.List;

public record DailyReportResponse(
        int totalCalories,

        List<MealResponseDTO> meals
) {
}
