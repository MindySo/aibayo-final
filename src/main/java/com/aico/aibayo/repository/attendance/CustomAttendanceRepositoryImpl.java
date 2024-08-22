package com.aico.aibayo.repository.attendance;

import com.aico.aibayo.dto.attendance.AttendanceDto;
import com.aico.aibayo.entity.QAttendanceEntity;
import com.aico.aibayo.entity.QKidEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class CustomAttendanceRepositoryImpl implements CustomAttendanceRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QAttendanceEntity attendance = QAttendanceEntity.attendanceEntity;
    private final QKidEntity kid = QKidEntity.kidEntity;

    @Override
    public List<AttendanceDto> findAllByKinderNoAndClassNo(Long kinderNo, Long classNo, LocalDate selectedDate) {
        return jpaQueryFactory
                .select(Projections.constructor(AttendanceDto.class,
                        attendance.kinderNo,
                        attendance.classNo,
                        kid.kidNo,
                        kid.kidName,
                        attendance.kidDrop,
                        attendance.kidPickup,
                        attendance.note,
                        attendance.attendanceStatus,
                        attendance.attendanceDate
                ))
                .from(attendance)
                .join(kid).on(attendance.kidNo.eq(kid.kidNo)
                        .and(attendance.kinderNo.eq(kid.kinderNo)))
                .where(attendance.kinderNo.eq(kinderNo)
                        .and(attendance.classNo.eq(classNo))
                        .and(attendance.attendanceDate.eq(selectedDate)))
                .fetch();
    }

}
