package com.aico.aibayo.service.attendance;

import com.aico.aibayo.dto.attendance.AttendanceDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.AttendanceEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface AttendanceService {
    List<AttendanceDto> getKids(Long kinderNo, Long classNo, LocalDate selectedDate);
    Long createAttendance(AttendanceDto dto);
    AttendanceEntity getAttendanceByKidNoAndDate(Long kidNo, LocalDate attendanceDate);

    void updateAttendance(Long kidNo, String attendanceStatus, LocalDateTime kidDrop, LocalDateTime kidPickup, String note, LocalDate attendanceDate, Long classNo, MemberDto memberDto);

}
