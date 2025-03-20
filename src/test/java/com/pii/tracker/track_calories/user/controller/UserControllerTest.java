package com.pii.tracker.track_calories.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;
import com.pii.tracker.track_calories.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestUser;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        var user = createTestUser();
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("vasYa")))
                .andExpect(jsonPath("$.email", is("vasYa@ya.ru")));
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        var users = List.of(createTestUser());
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is("vasYa")));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        var user = createTestUser();
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("vasYa")))
                .andExpect(jsonPath("$.email", is("vasYa@ya.ru")));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenEmailIsMissing() throws Exception {
        var invalidUser = User.builder()
                .id(2L)
                .name("noemail")
                .email(null)
                .weight(80)
                .height(175)
                .age(35)
                .weightGoal(WeightGoal.MAINTAIN)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email", containsString("Email обязателен")));
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenWeightGoalIsMissing() throws Exception {
        var invalidUser = User.builder()
                .name("NoGoalUser")
                .email("user@ya.ru")
                .weight(70)
                .height(175)
                .age(30)
                .weightGoal(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.weightGoal", containsString("Цель по изменению веса обязательна")));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenHeightIsTooHigh() throws Exception {
        var invalidUser = User.builder()
                .name("TallUser")
                .email("tall@ya.ru")
                .weight(80)
                .height(512)
                .age(35)
                .weightGoal(WeightGoal.MAINTAIN)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.height", containsString("Рост не может быть более 256 см")));
    }
}
