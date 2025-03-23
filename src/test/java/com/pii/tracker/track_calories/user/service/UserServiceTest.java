package com.pii.tracker.track_calories.user.service;

import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.model.WeightGoal;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.pii.tracker.track_calories.util.TestDataFactory.createTestUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CalorieCalculatorService calorieCalculatorService;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = createTestUser();
    }

    @Test
    void testCreateUser() {
        when(calorieCalculatorService.calculateDailyCalorieIntake(user)).thenReturn(2500);
        when(userRepository.save(user)).thenReturn(user);
        var createdUser = userService.createUser(user);
        assertNotNull(createdUser);
        assertEquals(2500, createdUser.getDailyCalorieIntake());
        verify(calorieCalculatorService, times(1)).calculateDailyCalorieIntake(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        var users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        var foundUser = userService.getUserById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        var foundUser = userService.getUserById(2L);
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(calorieCalculatorService.calculateDailyCalorieIntake(user)).thenReturn(2600);
        when(userRepository.save(user)).thenReturn(user);
        var updatedUser = User.builder()
                .name("updated")
                .email("updated@")
                .age(35)
                .weight(85)
                .height(180)
                .weightGoal(WeightGoal.LOSE)
                .build();

        var result = userService.updateUser(1L, updatedUser);
        assertNotNull(result);
        assertEquals("updated", result.getName());
        assertEquals(2600, result.getDailyCalorieIntake());
        verify(userRepository, times(1)).findById(1L);
        verify(calorieCalculatorService, times(1)).calculateDailyCalorieIntake(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        var updatedUser = User.builder()
                .name("updated")
                .email("updated@example.com")
                .age(35)
                .weight(85)
                .height(180)
                .weightGoal(WeightGoal.LOSE)
                .build();
        var unfException = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(2L, updatedUser);
        });
        assertEquals("Пользователь не найден id=2", unfException.getMessage());
        verify(userRepository, times(1)).findById(2L);
        verify(calorieCalculatorService, never()).calculateDailyCalorieIntake(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(2L)).thenReturn(false);
        var unfException = assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(2L);
        });
        assertEquals("Пользователь не найден id=2", unfException.getMessage());
        verify(userRepository, times(1)).existsById(2L);
        verify(userRepository, never()).deleteById(any());
    }
}
