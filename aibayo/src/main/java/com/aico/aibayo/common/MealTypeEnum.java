package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum MealTypeEnum {
    BREAKFAST(0, "아침"),
    MORNING_SNACK(1, "오전간식"),
    LUNCH(2, "점심"),
    AFTERNOON_SNACK(3, "오후간식"),
    DINNER(4, "저녁");

    private final int type;
    private final String name;

    MealTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public static MealTypeEnum findByType(int type) {
        for (MealTypeEnum mealType : MealTypeEnum.values()) {
            if (type == mealType.getType()) {
                return mealType;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }

    public static MealTypeEnum findByName(String name) {
        for (MealTypeEnum mealType : MealTypeEnum.values()) {
            if (name.equals(mealType.getName())) {
                return mealType;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
