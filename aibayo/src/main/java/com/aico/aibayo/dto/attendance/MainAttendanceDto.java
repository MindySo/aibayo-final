package com.aico.aibayo.dto.attendance;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MainAttendanceDto {
    private Long classNo;
    private String className;
    private boolean isExist;
    private int totalCount;
    private int presentCount;
    private int kidDropCount;
    private int kidPickupCount;
    private int absentCount;
}
