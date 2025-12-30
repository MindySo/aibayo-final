package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum MemberStatusEnum {
    INACTIVE(0),
    ACTIVE(1),
    TEMP(2); // 관리자 승인 전

    private final Integer status;

    MemberStatusEnum(Integer status) {
        this.status = status;
    }

    static MemberStatusEnum findByStatus(int status) {
        for (MemberStatusEnum memberStatusEnum : MemberStatusEnum.values()) {
            if (status == memberStatusEnum.status) {
                return memberStatusEnum;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
