package com.aico.aibayo.dto.medication;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MedicationDto {
    private Long orderNo;
    private Long kidNo;
    private Integer orderChecked;
    private Integer orderType;
    private LocalDateTime orderDeleteDate;
    private LocalDateTime orderRequestDate;
    private LocalDateTime runDate;
    private String orderDeleteFlag;
    private String orderParentSign;
    private String orderRequester;
    private String orderSpecific;

    private Long kinderNo;

    private Long mediOrderNo;
    private String symptoms;

    private Long mediDetailNo;
    private String dosage;
    private String doseCount;
    private String doseTime;
    private String mediType;
    private String storageType;

}
