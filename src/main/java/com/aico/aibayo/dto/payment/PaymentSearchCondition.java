package com.aico.aibayo.dto.payment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PaymentSearchCondition {
    private Long kinderNo;
    private Long MemberId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Integer> paymentStatusList;
    private String inputString;
    private Long classNo;
    private Long billNo;
}
