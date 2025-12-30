package com.aico.aibayo.dto.attendance;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttendanceDto {
    private Long kinderNo;
    private Long classNo; // 반 번호
    private Long kidNo; // 원생 번호
    private String kidName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime kidDrop; // 등원시간
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime kidPickup; // 하원시간
    private String note; // 비고
    private String attendanceStatus;
    private LocalDate attendanceDate;
}
