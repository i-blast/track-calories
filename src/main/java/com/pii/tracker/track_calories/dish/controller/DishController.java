package com.pii.tracker.track_calories.dish.controller;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.dish.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    @PostMapping
    public ResponseEntity<Dish> createDish(@Valid @RequestBody Dish dish) {
        return ResponseEntity.ok(dishService.createDish(dish));
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable Long id) {
        return dishService.getDishById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable Long id, @Valid @RequestBody Dish dish) {
        return ResponseEntity.ok(dishService.updateDish(id, dish));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
