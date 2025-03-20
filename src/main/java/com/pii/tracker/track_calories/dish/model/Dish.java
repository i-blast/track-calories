package com.pii.tracker.track_calories.dish.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название блюда не может быть пустым")
    @Size(max = 256, message = "Название блюда должно содержать не более 256 символов")
    private String name;

    @Min(value = 1, message = "Калорийность должна быть не менее 1 ккал")
    @Max(value = 2048, message = "Калорийность не может превышать 2048 ккал")
    private int calories;

    /// Жиры, белки и углеводы измеряем в граммах.
    @Min(value = 0, message = "Количество белков не может быть отрицательным")
    private int proteins;

    @Min(value = 0, message = "Количество жиров не может быть отрицательным")
    private int fats;

    @Min(value = 0, message = "Количество углеводов не может быть отрицательным")
    private int carbohydrates;

}
