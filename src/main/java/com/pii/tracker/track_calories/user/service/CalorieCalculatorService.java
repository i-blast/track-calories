package com.pii.tracker.track_calories.user.service;

import com.pii.tracker.track_calories.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class CalorieCalculatorService {

    /**
     * Набор коэффициентов для формулы Харриса-Бенедикта.
     */
    public static final double[] BMR_COEFFICIENTS = {88.362, 13.397, 4.799, 5.677};
    /**
     * Коэффициент активности "Легкие тренировки".
     */
    public static final double ACTIVITY_FACTOR = 1.375;

    /**
     * Расчёт целевого расхода калорий.
     *
     * @param user - пользователь.
     * @return значение целевого количества калорий.
     */
    public int calculateDailyCalorieIntake(User user) {
        // Формула Харриса-Бенедикта для мужчин.
        double bmr = BMR_COEFFICIENTS[0] +
                (BMR_COEFFICIENTS[1] * user.getWeight()) +
                (BMR_COEFFICIENTS[2] * user.getHeight()) -
                (BMR_COEFFICIENTS[3] * user.getAge());
        // Выбран некоторый коэффициент активности.
        int dailyCalories = (int) (bmr * ACTIVITY_FACTOR);

        return switch (user.getWeightGoal()) {
            // Дефицит калорий 15% массы тела.
            case LOSE -> (int) (dailyCalories * 0.85);
            // Профицит калорий 15% массы тела.
            case GAIN -> (int) (dailyCalories * 1.15);
            // Держим вес.
            case MAINTAIN -> dailyCalories;
        };
    }
}
