package com.pii.tracker.track_calories.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;
import com.pii.tracker.track_calories.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(properties = "spring.config.location=classpath:application-test.yaml")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        User user = createUser();
        when(userService.createUser(user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("vasYa")))
                .andExpect(jsonPath("$.email", is("vasYa@ya.ru")));
    }

    private User createUser() {
        return User.builder()
                .name("vasYa")
                .email("vasYa@ya.ru")
                .weight(80)
                .height(175)
                .age(35)
                .weightGoal(WeightGoal.LOSE)
                .build();
    }
}
