package com.pii.tracker.track_calories.user.service;

import com.pii.tracker.track_calories.user.exception.UserNotFoundException;
import com.pii.tracker.track_calories.user.model.User;
import com.pii.tracker.track_calories.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CalorieCalculatorService calorieCalculatorService;

    public User createUser(User user) {
        int dailyCalories = calorieCalculatorService.calculateDailyCalorieIntake(user);
        user.setDailyCalorieIntake(dailyCalories);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setWeight(updatedUser.getWeight());
            existingUser.setHeight(updatedUser.getHeight());
            existingUser.setWeightGoal(updatedUser.getWeightGoal());

            existingUser.setDailyCalorieIntake(calorieCalculatorService.calculateDailyCalorieIntake(existingUser));

            return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException("Пользователь не найден id=" + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Пользователь не найден id=" + id);
        }
        userRepository.deleteById(id);
    }
}
