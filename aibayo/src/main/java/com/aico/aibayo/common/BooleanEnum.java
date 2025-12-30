package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum BooleanEnum {
    TRUE("1"),
    FALSE("0");

    private String bool;

    BooleanEnum(String bool) {
        this.bool = bool;
    }

    static BooleanEnum findByValue(String bool) {
        for (BooleanEnum b : BooleanEnum.values()) {
            if (bool.equals(b.bool)) {
                return b;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
