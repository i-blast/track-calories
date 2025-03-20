package com.pii.tracker.track_calories.meal.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateMealRequestDTO(
        @NotNull(message = "Приём пищи связан с пользователем")
        Long userId,

        @NotEmpty(message = "Приём пищи должен содержать хотя бы одно блюдо")
        List<Long> dishIds
) {
}
