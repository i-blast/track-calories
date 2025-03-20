package com.pii.tracker.track_calories.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    private int age;

    @Min(value = 32, message = "Вес должен быть не менее 32 кг")
    @Max(value = 256, message = "Вес не может быть более 256 кг")
    private double weight;

    @Min(value = 64, message = "Рост должен быть не менее 64 см")
    @Max(value = 256, message = "Рост не может быть более 256 см")
    private double height;

    @NotNull(message = "Цель по изменению весу обязательна")
    @Enumerated(EnumType.STRING)
    @Column(name = "weight_goal")
    private WeightGoal weightGoal;

    private int dailyCalorieIntake;
}
