package com.aico.aibayo.dto.payment;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentLogDto {
    private Long paymentLogNo;
    private Long billNo;
    private Integer paymentStatus;
    private LocalDateTime paymentLogRegDate;
}
