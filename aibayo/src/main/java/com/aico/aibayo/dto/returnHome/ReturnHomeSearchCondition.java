package com.aico.aibayo.dto.returnHome;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReturnHomeSearchCondition {
    private Long orderNo;
    private Long kidNo;
    private Integer orderType;
    private Long kinderNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderRequestDate;
    @JsonFormat(pattern = "yyyy-MM-dd ")
    private LocalDateTime runDate;
    private String rhType;
    private String dischargeFlag;

}
