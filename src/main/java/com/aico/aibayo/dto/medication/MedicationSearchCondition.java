package com.aico.aibayo.dto.medication;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MedicationSearchCondition {
    private Long orderNo;
    private Long kidNo;
    private Integer orderType;
    private Long kinderNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderRequestDate;
    private LocalDateTime runDate;


}
