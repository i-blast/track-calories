package com.pii.tracker.track_calories.report.service;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.meal.mapper.MealMapper;
import com.pii.tracker.track_calories.meal.service.MealService;
import com.pii.tracker.track_calories.report.dto.DailyReportResponse;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.pii.tracker.track_calories.util.DateTimeUtils.endOfDay;
import static com.pii.tracker.track_calories.util.DateTimeUtils.startOfDay;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final MealService mealService;
    private final UserService userService;

    /**
     * Отчёт за день с суммой всех калорий и приёмов пищи.
     *
     * @param userId идентификатор пользователя
     * @param date дата, по которой формируется отчёт
     * @return отчёт
     */
    public DailyReportResponse getDailyReport(Long userId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден id=" + userId));

        var meals = mealService.getMealsByUserAndDateRange(userId, startOfDay(date), endOfDay(date));
        var totalCalories = meals.stream()
                .flatMap(meal -> meal.getDishes().stream())
                .mapToInt(Dish::getCalories)
                .sum();

        return new DailyReportResponse(
                totalCalories,
                new MealMapper().toDtoList(meals)
        );
    }
}
