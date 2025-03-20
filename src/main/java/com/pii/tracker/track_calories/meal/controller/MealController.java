package com.pii.tracker.track_calories.meal.controller;

import com.pii.tracker.track_calories.meal.dto.CreateMealRequestDTO;
import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;
import com.pii.tracker.track_calories.meal.service.MealService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @PostMapping
    public ResponseEntity<MealResponseDTO> createMeal(
            @Valid @RequestBody CreateMealRequestDTO dto
    ) {
        return ResponseEntity.ok(mealService.createMeal(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealResponseDTO>> getMealsByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(mealService.getMealsByUser(userId));
    }
}
