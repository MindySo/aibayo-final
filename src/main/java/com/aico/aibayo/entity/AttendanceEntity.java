package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttendanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attendance_no")
    private int attendanceNo; // 출석부 번호
    @Column(name = "attendance_date")
    private LocalDateTime attendanceDate; // 출석 날짜
    @Column(name = "class_no")
    private int classNo; // 반 번호
    @Column(name = "kid_no")
    private int kidNo; // 원생 번호
    @Column(name = "kinder_no")
    private int kinderNo; // 유치원 번호
    @Column(name = "kid_drop")
    private LocalDateTime kidDrop; // 등원시간
    @Column(name = "kid_pickup")
    private LocalDateTime kidPickup; // 하원시간
    @Column(name = "attendance_modify_date")
    private LocalDateTime attendanceModifyDate; // 출석 수정 일자
    @Column(name = "attendance_delete_date")
    private LocalDateTime attendanceDeleteDate; // 출석 삭제 일자
    @Column(name = "excused_absence")
    private String excusedAbsence; // 결석 사유
    @Column
    private String note; // 비고
}