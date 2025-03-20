package com.pii.tracker.track_calories.user.service;

import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CalorieCalculatorServiceTest {

    @InjectMocks
    private CalorieCalculatorService calorieCalculatorService;

    @ParameterizedTest
    @CsvSource({
            "80, 175, 35, LOSE, 2104",
            "80, 175, 35, MAINTAIN, 2476",
            "80, 175, 35, GAIN, 2847",
            "90, 175, 35, MAINTAIN, 2660",
            "80, 180, 35, MAINTAIN, 2509",
            "80, 175, 40, MAINTAIN, 2437"
    })
    public void testCalculateDailyCalorieIntake(int weight, int height, int age, WeightGoal weightGoal, int expectedCalories) {
        User user = User.builder()
                .weight(weight)
                .height(height)
                .age(age)
                .weightGoal(weightGoal)
                .build();
        int calories = calorieCalculatorService.calculateDailyCalorieIntake(user);
        assertEquals(expectedCalories, calories);
    }
}
