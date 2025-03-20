package com.pii.tracker.track_calories.dish.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.dish.service.DishService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DishController.class)
public class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private DishService dishService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDish_ShouldReturnCreatedDish() throws Exception {
        var dish = createDish();
        when(dishService.createDish(any(Dish.class))).thenReturn(dish);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dish)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Салат Цезарь с курицей"))
                .andExpect(jsonPath("$.calories").value(250));
    }

    @Test
    void createDish_ShouldReturnBadRequest_WhenNameIsEmpty() throws Exception {
        var invalidDish = Dish.builder()
                .name("")
                .calories(250)
                .proteins(15)
                .fats(18)
                .carbohydrates(8)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDish)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void getDishById_ShouldReturnDish_WhenExists() throws Exception {
        var dish = new Dish(1L, "Кекс с изюмом", 400, 7, 20, 60);
        when(dishService.getDishById(1L)).thenReturn(Optional.of(dish));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/dishes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Кекс с изюмом"))
                .andExpect(jsonPath("$.calories").value(400));
    }

    @Test
    void getDishById_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        when(dishService.getDishById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/dishes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDish_ShouldReturnBadRequest_WhenCaloriesExceedLimit() throws Exception {
        var invalidDish = new Dish(1L, "Стейк", 3000, 50, 40, 20);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDish)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.calories").exists());
    }

    @Test
    void createDish_ShouldReturnBadRequest_WhenProteinsAreNegative() throws Exception {
        Dish invalidDish = new Dish(1L, "Омлет", 400, -10, 20, 15);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDish)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.proteins").exists());
    }

    private Dish createDish() {
        return Dish.builder()
                .name("Салат Цезарь с курицей")
                .calories(250)
                .proteins(15)
                .fats(18)
                .carbohydrates(8)
                .build();
    }
}
