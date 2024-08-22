package com.aico.aibayo.dto;

import com.aico.aibayo.entity.RegisterKinderEntity;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private Long kinderNo; //유치원 번호
    private LocalTime kinderOpenTime; // 유치원 등원시간
    private LocalTime kinderCloseTime; //유치원 하원시간
    private String kinderCode; //유치원 코드
    private String kinderName; // 유치원 이름
    private LocalDateTime kinderRegDate; //유치원 등록일자
    private LocalDateTime kinderModifyDate; //유치원 수정일자
    private LocalDateTime kinderDeleteDate; //유치원 삭제일자
    private Integer announceStatus; //공지사항 상태
    private Integer notepadStatus; //알림장 상태
    private Integer mealStatus; //식단표 상태
    private Integer medicationStatus; //투약 의뢰서 상태
    private Integer returnHomeStatus; //귀가 동의서 상태
    private Integer attendanceStatus; //출석부 상태
    private Integer scheduleStatus; //일정표 상태
    private Integer pickDropStatus; //안심 승하차 상태
    private Integer lifeRecordStatus; //생활 기록 상태
    private Integer chatStatus; //열린소통 상태
    private String sidoList; //API 시도 리스트
    private String sggList; //API 시군구 리스트

    public static RegisterKinderDto toDto(RegisterKinderEntity entity) {
        return new RegisterKinderDto(
                entity.getKinderNo(),
                entity.getKinderOpenTime(),
                entity.getKinderCloseTime(),
                entity.getKinderCode(),
                entity.getKinderName(),
                entity.getKinderRegDate(),
                entity.getKinderModifyDate(),
                entity.getKinderDeleteDate(),
                entity.getAnnounceStatus(),
                entity.getNotepadStatus(),
                entity.getMealStatus(),
                entity.getMedicationStatus(),
                entity.getReturnHomeStatus(),
                entity.getAttendanceStatus(),
                entity.getScheduleStatus(),
                entity.getPickDropStatus(),
                entity.getLifeRecordStatus(),
                entity.getChatStatus(),
                entity.getSidoList(),
                entity.getSggList()
        );
    }
}
