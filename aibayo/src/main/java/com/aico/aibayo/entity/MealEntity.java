package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = {"mealNo", "kinderNo", "mealDate", "mealRegDate",
        "mealModifyDate", "mealDeleteDate", "mealDeleteFlag"})
public class MealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_no")
    private Long mealNo;
    @Column(name = "kinder_no")
    private Long kinderNo;
    @Column(name = "meal_date")
    private LocalDate mealDate;
    @Column(name = "meal_reg_date")
    private LocalDateTime mealRegDate;
    @Column(name = "meal_modify_date")
    private LocalDateTime mealModifyDate;
    @Column(name = "meal_delete_date")
    private LocalDateTime mealDeleteDate;
    @Column(name = "meal_delete_flag")
    private String mealDeleteFlag;

    @OneToMany(mappedBy = "meal")
    private List<MealDetailEntity> mealDetails = new ArrayList<>();
}
