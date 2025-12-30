package com.aico.aibayo.dto.returnHome;

import com.aico.aibayo.entity.OrderFormEntity;
import com.aico.aibayo.entity.ReturnHomeAgreementEntity;
import com.aico.aibayo.entity.ReturnHomeAgreementParentEntity;
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
//    public ReturnHomeDto (
//            Long orderNo, Integer orderType ,Long kidNo ,
//            LocalDateTime orderRequestDate , LocalDateTime runDate,
//            Long rhAgreeNo , LocalTime rhTime , String rhType , Long rhAgreeParentNo,
//            String rhMainParentName ,String rhMainParentTel ,
//            String rhMinorParentName ,String rhMinorParentTel ,
//            String kidName , String dischargeFlag
//    ){
//            this.orderNo=orderNo;
//            this.orderType=orderType;
//            this.kidNo=kidNo;
//            this.orderRequestDate=orderRequestDate;
//            this.runDate=runDate;
//            this.rhAgreeNo=rhAgreeNo;
//            this.rhTime=rhTime;
//            this.rhType=rhType;
//            this.rhAgreeParentNo=rhAgreeParentNo;
//            this.rhMainParentName=rhMainParentName;
//            this.rhMainParentTel=rhMainParentTel;
//            this.rhMinorParentName=rhMinorParentName;
//            this.rhMinorParentTel=rhMinorParentTel;
//            this.kidName=kidName;
//            this.dischargeFlag=dischargeFlag;
//    }

    public ReturnHomeDto (
            Long orderNo, Integer orderType ,Long kidNo ,
            LocalDateTime orderRequestDate , LocalDateTime runDate,
            String orderRequester, String orderSpecific,
            String orderParentSign, String orderDeleteFlag,
            Long rhAgreeNo , LocalTime rhTime , String rhType , Long rhAgreeParentNo,
            String rhMainParentName ,String rhMainParentTel ,
            String rhMinorParentName ,String rhMinorParentTel ,
            String kidName , String dischargeFlag
    ){
        this.orderNo=orderNo;
        this.orderType=orderType;
        this.kidNo=kidNo;
        this.orderRequestDate=orderRequestDate;
        this.runDate=runDate;
        this.orderRequester=orderRequester;
        this.orderSpecific=orderSpecific;
        this.orderParentSign=orderParentSign;
        this.orderDeleteFlag=orderDeleteFlag;
        this.rhAgreeNo=rhAgreeNo;
        this.rhTime=rhTime;
        this.rhType=rhType;
        this.rhAgreeParentNo=rhAgreeParentNo;
        this.rhMainParentName=rhMainParentName;
        this.rhMainParentTel=rhMainParentTel;
        this.rhMinorParentName=rhMinorParentName;
        this.rhMinorParentTel=rhMinorParentTel;
        this.kidName=kidName;
        this.dischargeFlag=dischargeFlag;
    }

    public static ReturnHomeDto toDto(OrderFormEntity order, ReturnHomeAgreementEntity home, ReturnHomeAgreementParentEntity parent) {
        ReturnHomeDto dto = new ReturnHomeDto();

        // OrderFormEntity 데이터를 ReturnHomeDto에 설정
        dto.setOrderNo(order.getOrderNo());
        dto.setOrderType(order.getOrderType());
        dto.setOrderRequester(order.getOrderRequester());
        dto.setOrderRequestDate(order.getOrderRequestDate());
        dto.setRunDate(order.getRunDate());
        dto.setKidNo(order.getKidNo());
        dto.setOrderChecked(order.getOrderChecked());
        dto.setOrderSpecific(order.getOrderSpecific());
        dto.setOrderParentSign(order.getOrderParentSign());
        dto.setOrderDeleteDate(order.getOrderDeleteDate());
        dto.setOrderDeleteFlag(order.getOrderDeleteFlag());

        // ReturnHomeAgreementEntity 데이터를 ReturnHomeDto에 설정
        dto.setRhAgreeNo(home.getRhAgreeNo());
        dto.setRhTime(home.getRhTime());
        dto.setRhType(home.getRhType());

        // ReturnHomeAgreementParentEntity 데이터를 ReturnHomeDto에 설정
        dto.setRhAgreeParentNo(parent.getRhAgreeParentNo());
        dto.setRhMainParentName(parent.getRhMainParentName());
        dto.setRhMainParentTel(parent.getRhMainParentTel());
        dto.setRhMinorParentName(parent.getRhMinorParentName());
        dto.setRhMinorParentTel(parent.getRhMinorParentTel());

        return dto;
    }

    public static ReturnHomeDto toDto(OrderFormEntity order) {
        if (order == null) {
            return null;
        }

        // ReturnHomeDto의 필드를 설정합니다.
        ReturnHomeDto dto = new ReturnHomeDto();

        dto.setOrderNo(order.getOrderNo());
        dto.setOrderType(order.getOrderType());
        dto.setOrderRequester(order.getOrderRequester());
        dto.setOrderRequestDate(order.getOrderRequestDate());
        dto.setRunDate(order.getRunDate());
        dto.setKidNo(order.getKidNo());
        dto.setOrderChecked(order.getOrderChecked());
        dto.setOrderSpecific(order.getOrderSpecific());
        dto.setOrderParentSign(order.getOrderParentSign());
        dto.setOrderDeleteDate(order.getOrderDeleteDate());
        dto.setOrderDeleteFlag(order.getOrderDeleteFlag());
        return dto;
    }


}
