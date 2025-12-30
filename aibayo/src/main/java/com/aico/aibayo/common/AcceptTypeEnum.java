package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum AcceptTypeEnum {
    CLASS_KID(0),
    PARENT_KID(1),
    CLASS_TEACHER(2),
    TEACHER_KINDER(3),
    INVITE_CODE(4);

    private final int type;

    AcceptTypeEnum(int type) {
        this.type = type;
    }

    static AcceptTypeEnum findByType(int type) {
        for (AcceptTypeEnum acceptType : AcceptTypeEnum.values()) {
            if (type == acceptType.type) {
                return acceptType;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
