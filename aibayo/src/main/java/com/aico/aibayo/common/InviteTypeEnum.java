package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum InviteTypeEnum {
    TEACHER(0, "선생님"),
    KID(1, "학부모");

    private final int type;
    private final String desc;

    InviteTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static InviteTypeEnum findByType(int type) {
        for (InviteTypeEnum inviteTypeEnum : InviteTypeEnum.values()) {
            if (type == inviteTypeEnum.getType()) {
                return inviteTypeEnum;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }

    public static InviteTypeEnum findByDesc(String desc) {
        for (InviteTypeEnum inviteTypeEnum : InviteTypeEnum.values()) {
            if (desc.equals(inviteTypeEnum.getDesc())) {
                return inviteTypeEnum;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
