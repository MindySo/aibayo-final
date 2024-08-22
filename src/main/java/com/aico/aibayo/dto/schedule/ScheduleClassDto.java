package com.aico.aibayo.dto.schedule;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleClassDto {
    private Long scheduleNo;
    private Long classNo;
    private String className;
}
