package com.aico.aibayo.repository.attendance;

import com.aico.aibayo.dto.attendance.AttendanceDto;

import java.time.LocalDate;
import java.util.List;

public interface CustomAttendanceRepository {
    List<AttendanceDto> findAllByKinderNoAndClassNo(Long kinderNo, Long classNo, LocalDate selectedDate);
}
