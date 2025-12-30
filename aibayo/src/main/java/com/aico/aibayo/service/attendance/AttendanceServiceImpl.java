package com.aico.aibayo.service.attendance;

import com.aico.aibayo.dto.attendance.AttendanceDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.AttendanceEntity;
import com.aico.aibayo.repository.attendance.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;

    @Override
    public List<AttendanceDto> getKids(Long kinderNo, Long classNo, LocalDate selectedDate) {
        return attendanceRepository.findAllByKinderNoAndClassNo(kinderNo, classNo, selectedDate);
    }

    @Override
    public List<AttendanceDto> getKidsByKinderNo(Long kinderNo, LocalDate selectedDate) {
        return attendanceRepository.findAllByKinderNo(kinderNo, selectedDate);
    }

    // kidNo와 attendanceDate를 기준으로 AttendanceEntity를 가져오는 메서드
    public AttendanceEntity getAttendanceByKidNoAndDate(Long kidNo, LocalDate attendanceDate) {
        log.info("Fetching AttendanceEntity for kidNo: {}, attendanceDate: {}", kidNo, attendanceDate);
        Optional<AttendanceEntity> optionalAttendance = attendanceRepository.findByKidNoAndAttendanceDate(kidNo, attendanceDate);
        return optionalAttendance.orElseThrow(() -> new RuntimeException("Attendance not found"));
    }

    public void updateAttendance(Long kidNo, String attendanceStatus, LocalDateTime kidDrop, LocalDateTime kidPickup, String note, LocalDate attendanceDate, Long classNo, MemberDto memberDto) {
        // 1. AttendanceEntity 가져오기
        AttendanceEntity attendanceEntity = getAttendanceByKidNoAndDate(kidNo, attendanceDate);

        // 2. 필요한 필드 업데이트
        log.info("Updating AttendanceEntity for kidNo: {}, attendanceDate: {}", kidNo, attendanceDate);
        attendanceEntity.setAttendanceStatus(attendanceStatus);
        attendanceEntity.setKidDrop(kidDrop);
        attendanceEntity.setKidPickup(kidPickup);
        attendanceEntity.setNote(note);

        // 3. 업데이트된 엔티티 저장
        attendanceRepository.save(attendanceEntity);
        log.info("AttendanceEntity updated and saved for kidNo: {}, attendanceDate: {}", kidNo, attendanceDate);
    }

    public void deleteAttendance(Long kidNo, LocalDate attendanceDate) {
        AttendanceEntity attendanceEntity = getAttendanceByKidNoAndDate(kidNo, attendanceDate);
        attendanceRepository.delete(attendanceEntity);
    }


    @Override
    public Long createAttendance(AttendanceDto dto) {
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setAttendanceDate(LocalDate.now());
        attendanceEntity.setKinderNo(dto.getKinderNo());
        attendanceEntity.setClassNo(dto.getClassNo());
        attendanceEntity.setKidNo(dto.getKidNo());
        attendanceEntity.setAttendanceStatus(dto.getAttendanceStatus());
        attendanceEntity.setKidDrop(dto.getKidDrop());
        attendanceEntity.setKidPickup(dto.getKidPickup());
        attendanceEntity.setNote(dto.getNote());
        AttendanceEntity save = attendanceRepository.save(attendanceEntity);
        System.out.println("create attendance done");
        return save.getAttendanceNo();
    }
}
