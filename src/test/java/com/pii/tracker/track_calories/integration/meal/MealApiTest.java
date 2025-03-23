package com.pii.tracker.track_calories.integration.meal;

import com.pii.tracker.track_calories.dish.repo.DishRepository;
import com.pii.tracker.track_calories.integration.BaseIntegrationTest;
import com.pii.tracker.track_calories.meal.dto.CreateMealRequestDTO;
import com.pii.tracker.track_calories.meal.repo.MealRepository;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestDish;
import static com.pii.tracker.track_calories.util.TestDataFactory.createTestUser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class MealApiTest extends BaseIntegrationTest {

    @Autowired
    MealRepository mealRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    DishRepository dishRepository;

    private Long userId;
    private Long dish1Id;
    private Long dish2Id;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        mealRepository.deleteAll();
        userRepository.deleteAll();
        dishRepository.deleteAll();

        var user = createTestUser();
        userRepository.save(user);
        userId = user.getId();
        var dish1 = createTestDish();
        dishRepository.save(dish1);
        dish1Id = dish1.getId();
        var dish2 = createTestDish();
        dishRepository.save(dish2);
        dish2Id = dish2.getId();
    }

    @Test
    void shouldCreateNewMealWhenMealIsValid() {
        RestAssured.given()
                .body(new CreateMealRequestDTO(userId, List.of(dish1Id, dish2Id)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("api/meals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldFailToCreateMealWithEmptyDishList() {
        RestAssured.given()
                .body(new CreateMealRequestDTO(userId, List.of()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("api/meals")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldGetMealsByUser() {
        RestAssured.given()
                .when()
                .get("api/meals/user/" + userId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    void shouldReturnNotFoundForNonExistingUserMeals() {
        RestAssured.given()
                .when()
                .get("api/meals/user/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(0));
    }
}
