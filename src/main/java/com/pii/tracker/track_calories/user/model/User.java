package com.pii.tracker.track_calories.user.model;

import jakarta.persistence.*;
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
    private String name;
    private String email;
    private int age;
    private double weight;
    private double height;
    @Enumerated(EnumType.STRING)
    @Column(name = "weight_goal")
    private WeightGoal weightGoal;
    private int dailyCalorieIntake;
}
