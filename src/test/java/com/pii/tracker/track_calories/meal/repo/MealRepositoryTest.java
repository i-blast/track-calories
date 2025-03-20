package com.pii.tracker.track_calories.meal.repo;

import com.pii.tracker.track_calories.BaseRepositoryTest;
import com.pii.tracker.track_calories.dish.repo.DishRepository;
import com.pii.tracker.track_calories.meal.model.Meal;
import com.pii.tracker.track_calories.user.repo.UserRepository;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestDish;
import static com.pii.tracker.track_calories.util.TestDataFactory.createTestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DataJpaTest
public class MealRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DishRepository dishRepository;

    @Test
    void testShouldSaveMealSuccessfully() {
        var user = createTestUser();
        var savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();

        var dish1 = createTestDish();
        var dish2 = createTestDish();
        var savedDish1 = dishRepository.save(dish1);
        var savedDish2 = dishRepository.save(dish2);
        assertNotNull(savedDish1.getId());
        assertNotNull(savedDish2.getId());

        var meal = Meal.builder()
                .mealTime(LocalDateTime.now().minusMinutes(10))
                .user(savedUser)
                .dishes(List.of(savedDish1, savedDish2))
                .build();
        Meal savedMeal = mealRepository.save(meal);

        assertNotNull(savedMeal.getId());
        assertThat(savedMeal.getMealTime()).isEqualTo(meal.getMealTime());
        assertThat(savedMeal.getUser()).isEqualTo(savedUser);
        assertThat(savedMeal.getDishes()).containsAll(List.of(savedDish1, savedDish2));
    }

}
