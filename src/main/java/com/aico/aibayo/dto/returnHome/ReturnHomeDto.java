package com.aico.aibayo.dto.returnHome;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReturnHomeDto {
    private Long orderNo;
    private Integer orderType;
    private String orderRequester;
    private LocalDateTime orderRequestDate;
    private LocalDateTime runDate;
    private Long kidNo;
    private Integer orderChecked;
    private String orderSpecific;
    private String orderParentSign;
    private LocalDateTime orderDeleteDate;
    private String orderDeleteFlag;
    private Long rhAgreeNo;
    private LocalTime rhTime;
    private String rhType;
    private Long rhAgreeParentNo;
    private String rhMainParentName;
    private String rhMainParentTel;
    private String rhMinorParentName;
    private String rhMinorParentTel;
    private String kidName;
    private String dischargeFlag;

    public ReturnHomeDto (
            String kidName , String rhType , LocalDateTime runDate,
            LocalTime rhTime,String orderRequester , LocalDateTime orderRequestDate,
            Long orderNo, Integer orderType, Long kidNo,
            String orderDeleteFlag,Long rhAgreeNo,String dischargeFlag
            ){
            this.kidName=kidName;
            this.rhType=rhType;
            this.runDate=runDate;
            this.rhTime=rhTime;
            this.orderRequester=orderRequester;
            this.orderRequestDate=orderRequestDate;
            this.orderNo=orderNo;
            this.orderType=orderType;
            this.kidNo=kidNo;
            this.orderDeleteFlag=orderDeleteFlag;
            this.rhAgreeNo=rhAgreeNo;
            this.dischargeFlag=dischargeFlag;
    }

}
