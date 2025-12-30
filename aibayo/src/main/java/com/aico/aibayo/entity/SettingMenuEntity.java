package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "setting_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SettingMenuEntity {
    @Id
    @Column(name = "kinder_no")
    private Long kinderNo; // 유치원 번호

    @Column(name = "announce_status")
    private String announceStatus; // 공지사항 상태

    @Column(name = "notepad_status")
    private String notepadStatus; // 알림장 상태

    @Column(name = "meal_status")
    private String mealStatus; // 식단표 상태

    @Column(name = "medication_status")
    private String medicationStatus; // 투약 의뢰서 상태

    @Column(name = "return_home_status")
    private String returnHomeStatus; // 귀가 동의서 상태

    @Column(name = "attendance_status")
    private String attendanceStatus; // 출석부 상태

    @Column(name = "schedule_status")
    private String scheduleStatus; // 일정표 상태

    @Column(name = "pick_drop_status")
    private String pickDropStatus; // 안심 승하차 상태

    @Column(name = "life_record_status")
    private String lifeRecordStatus; // 생활 기록 상태

    @Column(name = "chat_status")
    private String chatStatus; // 열린소통 상태
}
