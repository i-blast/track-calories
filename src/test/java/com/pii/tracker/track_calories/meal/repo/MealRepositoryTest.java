package com.pii.tracker.track_calories.meal.repo;

import com.pii.tracker.track_calories.dish.repo.DishRepository;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestDish;
import static com.pii.tracker.track_calories.util.TestDataFactory.createTestUser;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MealRepositoryTest {

    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DishRepository dishRepository;

    @Test
    public void testSave() {
        var user = userRepository.save(createTestUser());
        var dish1 = dishRepository.save(createTestDish());
        var dish2 = dishRepository.save(createTestDish());
        var meal = Meal.builder()
                .mealTime(LocalDateTime.now())
                .user(user)
                .dishes(List.of(dish1, dish2))
                .build();
        var savedMeal = mealRepository.save(meal);
        assertThat(savedMeal.getId()).isNotNull();
        assertThat(savedMeal.getDishes()).hasSize(2);
    }
}
