package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meal_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MealTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_type_no")
    private Long mealTypeNo;
    @Column(name = "meal_type_name")
    private String mealTypeName;
}
