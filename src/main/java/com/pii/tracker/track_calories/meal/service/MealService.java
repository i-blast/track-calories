package com.pii.tracker.track_calories.meal.service;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.dish.repo.DishRepository;
import com.pii.tracker.track_calories.meal.dto.CreateMealRequestDTO;
import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;
import com.pii.tracker.track_calories.meal.exception.BadMealCreationException;
import com.pii.tracker.track_calories.meal.mapper.MealMapper;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.meal.repo.MealRepository;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository;
    private final MealMapper mealMapper;

    public MealResponseDTO createMeal(CreateMealRequestDTO dto) {
        User user = userRepository
                .findById(dto.userId())
                .orElseThrow(() -> new UserNotFoundException());
        if (dto.dishIds() == null || dto.dishIds().isEmpty()) {
            throw new BadMealCreationException("Прием пищи должен содержать хотя бы одно блюдо");
        }
        List<Dish> dishes = dishRepository.findAllById(dto.dishIds());
        if (dishes.size() != dto.dishIds().size()) {
            throw new BadMealCreationException("Некоторые из переданных блюд не существуют");
        }

        Meal meal = Meal.builder()
                .user(user)
                .dishes(dishes)
                .mealTime(LocalDateTime.now())
                .build();
        mealRepository.save(meal);

        return mealMapper.toDto(meal);
    }

    public List<MealResponseDTO> getMealsByUser(Long userId) {
        var meals = mealRepository.findByUserId(userId);
        return mealMapper.toDtoList(meals);
    }
}
