package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum PaymentStatusEnum {
    BILLED(0),
    PAID(1),
    OVERDUE(2),
    CANCEL(3),
    REFUND(4);

    private final int status;

    PaymentStatusEnum(int status) {
        this.status = status;
    }

    static PaymentStatusEnum findByStatus(int status) {
        for (PaymentStatusEnum paymentStatus : PaymentStatusEnum.values()) {
            if (status == paymentStatus.status) {
                return paymentStatus;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
