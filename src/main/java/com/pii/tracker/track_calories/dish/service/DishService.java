package com.pii.tracker.track_calories.dish.service;

import com.pii.tracker.track_calories.dish.exception.DishNotFoundException;
import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.dish.repo.DishRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DishService {

    private final DishRepository dishRepository;

    public Dish createDish(@Valid Dish dish) {
        return dishRepository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Optional<Dish> getDishById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish updateDish(Long id, @Valid Dish updatedDish) {
        return dishRepository.findById(id)
                .map(existingDish -> {
                    existingDish.setName(updatedDish.getName());
                    existingDish.setCalories(updatedDish.getCalories());
                    existingDish.setProteins(updatedDish.getProteins());
                    existingDish.setFats(updatedDish.getFats());
                    existingDish.setCarbohydrates(updatedDish.getCarbohydrates());
                    return dishRepository.save(existingDish);
                })
                .orElseThrow(() -> new DishNotFoundException("Блюдо не найдено id=" + id));
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }
}
