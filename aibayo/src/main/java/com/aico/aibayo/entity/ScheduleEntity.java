package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_no")
    private Long scheduleNo;
    @Column(name = "board_no")
    private Long boardNo;
    @Column(name = "schedule_start_date")
    private LocalDateTime scheduleStartDate;
    @Column(name = "schedule_end_date")
    private LocalDateTime scheduleEndDate;
    @Column(name = "map_lat")
    private Double mapLat;
    @Column(name = "map_lng")
    private Double mapLng;
}
