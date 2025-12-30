package com.aico.aibayo.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.aico.aibayo.entity.RegisterKinderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RegisterKinderDto {
    private Long kinderNo; // 유치원 번호
    private LocalTime kinderOpenTime; // 유치원 등원시간
    private LocalTime kinderCloseTime; // 유치원 하원시간
    private String kinderName; // 유치원 이름
    private String kinderPostCode; //유치원 우편번호
    private String kinderLocNo; //유치원 지역 번호
    private String kinderMidNo;
    private String kinderEndNo;
    private String kinderAddr; //유치원 주소
    private String kinderAddrDetail; //유치원 주소
    private LocalDateTime kinderRegDate; // 유치원 등록일자
    private LocalDateTime kinderModifyDate; // 유치원 수정일자
    private LocalDateTime kinderDeleteDate; // 유치원 삭제일자
    private String sidoList; // API 시도 리스트
    private String sggList; // API 시군구 리스트
    private String deleteFlag;
    private String principalName;

    public RegisterKinderDto(Long kinderNo, LocalTime kinderOpenTime, LocalTime kinderCloseTime, String kinderName,
                             String kinderPostCode, String kinderLocNo, String kinderMidNo, String kinderEndNo,
                             String kinderAddr, String kinderAddrDetail, LocalDateTime kinderRegDate,
                             LocalDateTime kinderModifyDate, LocalDateTime kinderDeleteDate,
                             String sidoList, String sggList, String deleteFlag) {
        this.kinderNo = kinderNo;
        this.kinderOpenTime = kinderOpenTime;
        this.kinderCloseTime = kinderCloseTime;
        this.kinderName = kinderName;
        this.kinderPostCode = kinderPostCode;
        this.kinderLocNo = kinderLocNo;
        this.kinderMidNo = kinderMidNo;
        this.kinderEndNo = kinderEndNo;
        this.kinderAddr = kinderAddr;
        this.kinderAddrDetail = kinderAddrDetail;
        this.kinderRegDate = kinderRegDate;
        this.kinderModifyDate = kinderModifyDate;
        this.kinderDeleteDate = kinderDeleteDate;
        this.sidoList = sidoList;
        this.sggList = sggList;
        this.deleteFlag = deleteFlag;
    }

    public static RegisterKinderDto toDto(RegisterKinderEntity entity) {
        if (entity == null) { return null; }

        return new RegisterKinderDto(
                entity.getKinderNo(),
                entity.getKinderOpenTime(),
                entity.getKinderCloseTime(),
                entity.getKinderName(),
                entity.getKinderPostCode(),
                entity.getKinderLocNo(),
                entity.getKinderMidNo(),
                entity.getKinderEndNo(),
                entity.getKinderAddr(),
                entity.getKinderAddrDetail(),
                entity.getKinderRegDate(),
                entity.getKinderModifyDate(),
                entity.getKinderDeleteDate(),
                entity.getSidoList(),
                entity.getSggList(),
                entity.getDeleteFlag()
        );
    }
}
