package com.pii.tracker.track_calories.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;
import com.pii.tracker.track_calories.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = createTestUser();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenEmailIsMissing() throws Exception {
        var invalidUser = User.builder()
                .name("noemail")
                .email(null)
                .weight(80)
                .height(175)
                .age(35)
                .weightGoal(WeightGoal.MAINTAIN)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_ShouldReturnUserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())));
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenThrow(new UserNotFoundException("Пользователь не найден id=1"));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException("Пользователь не найден id=1")).when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}