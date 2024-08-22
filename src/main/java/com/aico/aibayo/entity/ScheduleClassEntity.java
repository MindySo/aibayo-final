package com.aico.aibayo.entity;

import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.schedule.ScheduleClassDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "schedule_class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(ScheduleClassId.class)
public class ScheduleClassEntity {
    @Id
    @Column(name = "scheduleNo")
    private Long scheduleNo;
    @Id
    @Column(name = "class_no")
    private Long classNo;

}
