package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "register_kinder_bak")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterKinderBakEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="kinder_no")
    private Long kinderNo; //유치원 번호
    @Column(name="kinder_open_time")
    private LocalTime kinderOpenTime; // 유치원 등원시간
    @Column(name="kinder_close_time")
    private LocalTime kinderCloseTime; //유치원 하원시간
    @Column(name="kinder_code")
    private String kinderCode; //유치원 코드
    @Column(name = "kinder_name")
    private String kinderName; // 유치원 이름
    @Column(name="kinder_reg_date")
    private LocalDateTime kinderRegDate; //유치원 등록일자
    @Column(name="kinder_modify_date")
    private LocalDateTime kinderModifyDate; //유치원 수정일자
    @Column(name="kinder_delete_date")
    private LocalDateTime kinderDeleteDate; //유치원 삭제일자
    @Column(name="announce_status")
    private Integer announceStatus; //공지사항 상태
    @Column(name="notepad_status")
    private Integer notepadStatus; //알림장 상태
    @Column(name="meal_status")
    private Integer mealStatus; //식단표 상태
    @Column(name="medication_status")
    private Integer medicationStatus; //투약 의뢰서 상태
    @Column(name="return_home_status")
    private Integer returnHomeStatus; //귀가 동의서 상태
    @Column(name="attendance_status")
    private Integer attendanceStatus; //출석부 상태
    @Column(name="schedule_status")
    private Integer scheduleStatus; //일정표 상태
    @Column(name="pick_drop_status")
    private Integer pickDropStatus; //안심 승하차 상태
    @Column(name="life_record_status")
    private Integer lifeRecordStatus; //생활 기록 상태
    @Column(name="chat_status")
    private Integer chatStatus; //열린소통 상태
    @Column (name="sido_list")
    private String sidoList; //API 시도 리스트
    @Column (name="sgg_list")
    private String sggList; //API 시군구 리스트


}
