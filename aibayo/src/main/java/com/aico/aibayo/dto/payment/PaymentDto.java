package com.aico.aibayo.dto.payment;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long billNo;
    private Long kidNo;
    private Long classNo;
    private Long kinderNo;
    private Long discountRate;
    private String paymentTitle;
    private Long paymentPrice;
    private LocalDateTime paymentStartDate;
    private LocalDateTime paymentEndDate;
    private String paymentMemo;

    private Integer paymentStatus;
    private LocalDateTime paymentLogRegDate;

    private String kidName;
    private String className;

    private List<PaymentLogDto> paymentLogs;

    private Long id;
    private String name;

    public PaymentDto(Long billNo, Long kidNo, Long classNo, Long discountRate,
                       String paymentTitle, Long paymentPrice,
                       LocalDateTime paymentStartDate, LocalDateTime paymentEndDate,
                      String paymentMemo, Integer paymentStatus, LocalDateTime paymentLogRegDate,
                      String kidName, Long id, String className){
        this.billNo = billNo;
        this.kidNo = kidNo;
        this.classNo = classNo;
        this.discountRate = discountRate;
        this.paymentTitle = paymentTitle;
        this.paymentPrice = paymentPrice;
        this.paymentStartDate = paymentStartDate;
        this.paymentEndDate = paymentEndDate;
        this.paymentMemo = paymentMemo;
        this.paymentStatus = paymentStatus;
        this.paymentLogRegDate = paymentLogRegDate;
        this.kidName = kidName;
        this.id = id;
        this.className = className;
    }


    public PaymentDto(Long billNo, Long kidNo, Long classNo, Long discountRate,
                      String paymentTitle, Long paymentPrice,
                      LocalDateTime paymentStartDate, LocalDateTime paymentEndDate,
                      String paymentMemo, Integer paymentStatus, LocalDateTime paymentLogRegDate,
                      String kidName, String className){
        this.billNo = billNo;
        this.kidNo = kidNo;
        this.classNo = classNo;
        this.discountRate = discountRate;
        this.paymentTitle = paymentTitle;
        this.paymentPrice = paymentPrice;
        this.paymentStartDate = paymentStartDate;
        this.paymentEndDate = paymentEndDate;
        this.paymentMemo = paymentMemo;
        this.paymentStatus = paymentStatus;
        this.paymentLogRegDate = paymentLogRegDate;
        this.kidName = kidName;
        this.className = className;
    }

    public PaymentDto(Long kidNo, String kidName, Long id, Long discountRate){
        this.kidNo = kidNo;
        this.kidName = kidName;
        this.id = id;
        this.discountRate = discountRate;
    }

    public PaymentDto(Long kidNo, String kidName, Long id, String paymentMemo){
        this.kidNo = kidNo;
        this.kidName = kidName;
        this.id = id;
        this.paymentMemo = paymentMemo;
    }

    public PaymentDto(Long id, String name, Long kidNo, String kidName, Long classNo, String className,
                      String paymentTitle, Long discountRate, Long paymentPrice,
                      LocalDateTime paymentEndDate, String paymentMemo, Long kinderNo){
        this.id = id;
        this.name = name;
        this.kidNo = kidNo;
        this.kidName = kidName;
        this.classNo = classNo;
        this.className = className;
        this.paymentTitle = paymentTitle;
        this.discountRate = discountRate;
        this.paymentPrice = paymentPrice;
        this.paymentEndDate = paymentEndDate;
        this.paymentMemo = paymentMemo;
        this.kinderNo = kinderNo;
    }

    public PaymentDto(Long id, Long kidNo, Long classNo,
                      String paymentTitle, Long discountRate, Long paymentPrice,
                      LocalDateTime paymentEndDate, String paymentMemo, Long kinderNo){
        this.id = id;
        this.kidNo = kidNo;
        this.classNo = classNo;
        this.paymentTitle = paymentTitle;
        this.discountRate = discountRate;
        this.paymentPrice = paymentPrice;
        this.paymentEndDate = paymentEndDate;
        this.paymentMemo = paymentMemo;
        this.kinderNo = kinderNo;
    }

}
