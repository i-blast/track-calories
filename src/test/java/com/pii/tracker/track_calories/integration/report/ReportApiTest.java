package com.pii.tracker.track_calories.integration.report;

import com.pii.tracker.track_calories.dish.repo.DishRepository;
import com.pii.tracker.track_calories.integration.BaseIntegrationTest;
import com.pii.tracker.track_calories.meal.repo.MealRepository;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.pii.tracker.track_calories.util.TestDataFactory.*;
import static org.hamcrest.Matchers.*;

public class ReportApiTest extends BaseIntegrationTest {

    @Autowired
    MealRepository mealRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DishRepository dishRepository;

    private Long userId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        mealRepository.deleteAll();
        userRepository.deleteAll();
        dishRepository.deleteAll();

        var user = userRepository.save(createTestUser());
        userId = user.getId();
        var dish1 = dishRepository.save(createTestDish(500));
        var dish2 = dishRepository.save(createTestDish(700));
        var meal = createTestMeal(user, List.of(dish1, dish2), LocalDateTime.now());
        mealRepository.save(meal);
    }

    @Test
    void shouldGetDailyReport() {
        RestAssured.given()
                .when()
                .get("api/users/" + userId + "/reports/daily?date=" + LocalDate.now())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("totalCalories", equalTo(1200))
                .body("meals.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void shouldCheckCalorieLimit() {
        RestAssured.given()
                .when()
                .get("api/users/" + userId + "/reports/is-within-limit?date=" + LocalDate.now())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("isWithinLimit", is(true));
    }

    @Test
    void shouldFailForNonExistingUser() {
        RestAssured.given()
                .when()
                .get("api/users/99999/reports/daily?date=" + LocalDate.now())
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldFailForInvalidDateRange() {
        LocalDate startDate = LocalDate.now().plusDays(3L);
        LocalDate endDate = LocalDate.now().plusDays(2L);
        RestAssured.given()
                .when()
                .get("api/users/" + userId + "/reports/history?start=" + startDate + "&end=" + endDate)
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldGetMealHistory() {
        LocalDate startDate = LocalDate.now().minusDays(7L);
        LocalDate endDate = LocalDate.now().plusDays(2L);
        RestAssured.given()
                .when()
                .get("api/users/" + userId + "/reports/history?start=" + startDate + "&end=" + endDate)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(10))
                .body("[7].totalCalories", equalTo(1200));
    }
}
