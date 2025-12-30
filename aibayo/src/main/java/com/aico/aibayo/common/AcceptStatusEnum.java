package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum AcceptStatusEnum {
    WAIT(0),
    ACCEPT(1),
    INVITE(2),
    REJECT(3),
    DELETE(4);

    private final int status;

    AcceptStatusEnum(int status) {
        this.status = status;
    }

    static AcceptStatusEnum findByStatus(int status) {
        for (AcceptStatusEnum acceptStatus : AcceptStatusEnum.values()) {
            if (status == acceptStatus.status) {
                return acceptStatus;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
