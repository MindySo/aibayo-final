package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum MemberRoleEnum {
    ADMIN(0, ""),
    PRINCIPAL(1, "원장님"),
    TEACHER(2, "선생님"),
    PARENT(3, "학부모님");

    private final int role;
    private final String name;

    MemberRoleEnum(int role, String name) {
        this.role = role;
        this.name = name;
    }

    public static MemberRoleEnum findByType(int role) {
        for (MemberRoleEnum memberRole : MemberRoleEnum.values()) {
            if (role == memberRole.role) {
                return memberRole;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
