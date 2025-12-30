package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum OrderTypeEnum {
    MEDICATION(0),
    RETURNHOME(1);


    private int num;

    OrderTypeEnum(int num) {
        this.num = num;
    }

    static OrderTypeEnum findByNumber(int num) {
        for (OrderTypeEnum orderType : OrderTypeEnum.values()) {
            if (num == orderType.num) {
                return orderType;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
