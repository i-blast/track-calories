package com.pii.tracker.track_calories.report.service;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.meal.mapper.MealMapper;
import com.pii.tracker.track_calories.meal.service.MealService;
import com.pii.tracker.track_calories.report.dto.CalorieLimitResponse;
import com.pii.tracker.track_calories.report.dto.DailyReportResponse;
import com.pii.tracker.track_calories.report.dto.MealHistoryResponse;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Проверка уложился ли пользователь в дневное ограничение калорий.
     *
     * @param userId идентификатор пользователя
     * @param date дата, по которой формируется отчёт
     * @return отчёт
     */
    public CalorieLimitResponse checkCalorieLimit(Long userId, LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        var user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден id=" + userId));

        var totalCalories = mealService.getMealsByUserAndDateRange(userId, startOfDay(date), endOfDay(date))
                .stream()
                .flatMap(meal -> meal.getDishes().stream())
                .mapToInt(Dish::getCalories)
                .sum();
        return new CalorieLimitResponse(
                totalCalories <= user.getDailyCalorieIntake()
        );
    }

    /**
     * Получение истории приёмов пищи в диапазоне дат.
     *
     * @param userId пользователь
     * @param start начальная дата
     * @param end конечная дата (включительно)
     * @return отчёт
     */
    public List<MealHistoryResponse> getMealHistory(Long userId, LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Некорректный диапазон дат");
        }
        userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден id=" + userId));

        List<MealHistoryResponse> history = new ArrayList<>();
        for (var date = start; !date.isAfter(end); date = date.plusDays(1)) {
            var meals = mealService.getMealsByUserAndDateRange(userId, startOfDay(date), endOfDay(date));
            int totalCalories = meals.stream()
                    .flatMap(meal -> meal.getDishes().stream())
                    .mapToInt(Dish::getCalories)
                    .sum();
            history.add(new MealHistoryResponse(date, totalCalories, new MealMapper().toDtoList(meals)));
        }
        return history;
    }
}
