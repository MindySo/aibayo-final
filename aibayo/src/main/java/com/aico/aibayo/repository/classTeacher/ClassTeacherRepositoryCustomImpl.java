package com.aico.aibayo.repository.classTeacher;

import com.aico.aibayo.common.AcceptTypeEnum;
import com.aico.aibayo.dto.classTeacher.ClassTeacherDto;
import com.aico.aibayo.entity.QAcceptLogEntity;
import com.aico.aibayo.entity.QClassEntity;
import com.aico.aibayo.entity.QClassTeacherEntity;
import com.aico.aibayo.entity.QMemberEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ClassTeacherRepositoryCustomImpl implements ClassTeacherRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private final QAcceptLogEntity acceptLog = QAcceptLogEntity.acceptLogEntity;
    private final QClassTeacherEntity classTeacher = QClassTeacherEntity.classTeacherEntity;
    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QClassEntity classE = QClassEntity.classEntity;

    @Override
    public List<ClassTeacherDto> findAllByClassNo(Long classNo) {
        return jpaQueryFactory
                .select(Projections.constructor(ClassTeacherDto.class,
                        classTeacher.classNo,
                        classTeacher.classTeacherId,
                        classTeacher.acceptNo,
                        member.name,
                        acceptLog.acceptStatus,
                        classE.className
                        ))
                .from(classTeacher)
                .join(acceptLog).on(acceptLog.acceptNo.eq(classTeacher.acceptNo)
                        .and(acceptLog.acceptStatus.eq(1))
                        .and(acceptLog.acceptType.eq(AcceptTypeEnum.CLASS_TEACHER.getType())))
                .join(member).on(member.id.eq(classTeacher.classTeacherId))
                .join(classE).on(classE.classNo.eq(classTeacher.classNo))
                .where(classTeacher.classNo.eq(classNo))
                .fetch();
    }
}
