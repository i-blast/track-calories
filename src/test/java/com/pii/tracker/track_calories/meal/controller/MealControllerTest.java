package com.pii.tracker.track_calories.meal.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.tracker.track_calories.meal.dto.CreateMealRequestDTO;
import com.pii.tracker.track_calories.meal.dto.MealResponseDTO;
import com.pii.tracker.track_calories.meal.service.MealService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = MealController.class)
public class MealControllerTest {

    @MockitoBean
    private MealService mealService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createMeal_ShouldReturnCreatedMeal() throws Exception {
        var requestDTO = new CreateMealRequestDTO(1L, List.of(1L, 2L));
        var responseDTO = new MealResponseDTO(1L, LocalDateTime.now(), 1L, List.of("Pasta", "Salad"));
        when(mealService.createMeal(any(CreateMealRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.mealTime").exists())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.dishNames[0]").value("Pasta"))
                .andExpect(jsonPath("$.dishNames[1]").value("Salad"));
    }

    @Test
    void createMeal_ShouldReturnBadRequest_WhenUserIdIsNull() throws Exception {
        var requestDTO = new CreateMealRequestDTO(null, List.of(1L, 2L));
        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMeal_ShouldReturnBadRequest_WhenDishIdsIsEmpty() throws Exception {
        var requestDTO = new CreateMealRequestDTO(1L, List.of());
        mockMvc.perform(post("/api/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getMealsByUser_ShouldReturnMeals() throws Exception {
        var responseDTO = new MealResponseDTO(1L, LocalDateTime.now(), 1L, List.of("Pasta", "Salad"));
        var meals = List.of(responseDTO);
        when(mealService.getMealsByUser(1L)).thenReturn(meals);

        mockMvc.perform(get("/api/meals/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].mealTime").exists())
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].dishNames[0]").value("Pasta"))
                .andExpect(jsonPath("$[0].dishNames[1]").value("Salad"));
    }
}
