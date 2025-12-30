package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum KidGenderEnum {
    MALE(1, "남아"),
    FEMALE(2, "여아");

    private final int type;
    private final String name;

    KidGenderEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    static KidGenderEnum findByNumber(int type) {
        for (KidGenderEnum kidGender : KidGenderEnum.values()) {
            if (type == kidGender.type) {
                return kidGender;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }

    static KidGenderEnum findByName(String name) {
        for (KidGenderEnum kidGender : KidGenderEnum.values()) {
            if (name.equals(kidGender.name)) {
                return kidGender;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
