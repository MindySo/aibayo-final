package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meal_detail")
@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString(of = {"mealDetailNo", "mealType", "mealMenu", "mealPic",
        "mealPicOriginalName", "mealInvisibleFlag"})
public class MealDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_detail_no")
    private Long mealDetailNo;
    @Column(name = "meal_type")
    private Integer mealType;
    @Column(name = "meal_menu")
    private String mealMenu;
    @Column(name = "meal_pic", columnDefinition = "LONGTEXT")
    private String mealPic;
    @Column(name = "meal_pic_original_name")
    private String mealPicOriginalName;
    @Column(name = "meal_invisible_flag")
    private String mealInvisibleFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_no")
    private MealEntity meal;

    public MealDetailEntity(Long mealDetailNo, Integer mealType, String mealMenu, String mealPic,
                            String mealPicOriginalName, String mealInvisibleFlag, MealEntity meal) {
        this.mealDetailNo = mealDetailNo;
        this.mealType = mealType;
        this.mealMenu = mealMenu;
        this.mealPic = mealPic;
        this.mealPicOriginalName = mealPicOriginalName;
        this.mealInvisibleFlag = mealInvisibleFlag;
        if (meal != null) {
            changeMeal(meal);
        }
    }

    public void changeMeal(MealEntity meal) {
        this.meal = meal;
        meal.getMealDetails().add(this);
    }
}
