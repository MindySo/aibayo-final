package com.aico.aibayo.repository.attendance;

import com.aico.aibayo.dto.attendance.AttendanceDto;
import com.aico.aibayo.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Integer>, CustomAttendanceRepository {
    Optional<AttendanceEntity> findByKidNoAndAttendanceDate(Long kidNo, LocalDate attendanceDate);
}
