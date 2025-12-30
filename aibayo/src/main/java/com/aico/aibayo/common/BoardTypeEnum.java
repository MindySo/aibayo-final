package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum BoardTypeEnum {
    NOTEPAD(0),
    ANNOUNCE(1),
    SCHEDULE(2);

    private int num;

    BoardTypeEnum(int num) {
        this.num = num;
    }

    static BoardTypeEnum findByNumber(int num) {
        for (BoardTypeEnum boardType : BoardTypeEnum.values()) {
            if (num == boardType.num) {
                return boardType;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
