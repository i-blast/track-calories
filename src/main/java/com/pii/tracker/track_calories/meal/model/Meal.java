package com.pii.tracker.track_calories.meal.model;

import com.pii.tracker.track_calories.dish.model.Dish;
import com.pii.tracker.track_calories.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "У приёма пищи должны быть дата и время")
    @PastOrPresent(message = "Дата и время приёма пищи ограничены произошедшим временем")
    private LocalDateTime mealTime;

    @NotNull(message = "Приём пищи должен быть связан с пользователем")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotEmpty(message = "Приём пищи должен содержать хотя бы одно блюдо")
    @ManyToMany
    @JoinTable(
            name = "meal_dishes",
            joinColumns = @JoinColumn(name = "meal_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> dishes;

}
