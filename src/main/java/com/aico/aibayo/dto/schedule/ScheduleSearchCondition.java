package com.aico.aibayo.dto.schedule;

import com.aico.aibayo.dto.ClassDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleSearchCondition {

    private Long kinderNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime selectedDate;

    private Long scheduleNo;
    private Long classNo;

    private List<ClassDto> classDtos;

}
