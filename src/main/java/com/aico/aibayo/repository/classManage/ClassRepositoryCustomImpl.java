package com.aico.aibayo.repository.classManage;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.schedule.ScheduleSearchCondition;
import com.aico.aibayo.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassRepositoryCustomImpl implements ClassRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    private final QClassEntity clazz = QClassEntity.classEntity;
    private final QClassTeacherEntity classTeacher = QClassTeacherEntity.classTeacherEntity;
    private final QAcceptLogEntity acceptLog = QAcceptLogEntity.acceptLogEntity;
    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QClassKidEntity classKid = QClassKidEntity.classKidEntity;


    @Override
    public List<ClassDto> findAllByMemberId(Long id) {
        return jpaQueryFactory
                .select(Projections.constructor(ClassDto.class,
                        clazz.classNo,
                        clazz.className,
                        clazz.classAge,
                        clazz.kinderNo,
                        clazz.classRegDate,
                        clazz.classModifyDate,
                        clazz.classDeleteDate,
                        clazz.classDeleteFlag
                        ))
                .from(clazz)
                .join(classTeacher).on(clazz.classNo.eq(classTeacher.classNo))
                .join(acceptLog).on(acceptLog.acceptNo.eq(classTeacher.acceptNo))
                .join(member).on(
                        member.id.eq(classTeacher.classTeacherId),
                        member.kinderNo.eq(clazz.kinderNo)
                ).where(
                        clazz.classDeleteFlag.eq(BooleanEnum.FALSE.getBool()),
                        acceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        member.id.eq(id)
                ).fetch();
    }

    @Override
    public List<ClassDto> findAllByKidNo(Long kidNo) {
        return jpaQueryFactory
                .select(Projections.constructor(ClassDto.class,
                        clazz.classNo,
                        clazz.className,
                        clazz.classAge,
                        clazz.kinderNo,
                        clazz.classRegDate,
                        clazz.classModifyDate,
                        clazz.classDeleteDate,
                        clazz.classDeleteFlag,
                        acceptLog.acceptNo
                        ))
                .from(clazz)
                .join(classKid).on(clazz.classNo.eq(classKid.classNo))
                .join(acceptLog).on(acceptLog.acceptNo.eq(classKid.acceptNo))
                .where(
                        clazz.classDeleteFlag.eq(BooleanEnum.FALSE.getBool()),
                        acceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        classKid.kidNo.eq(kidNo)
                )
                .fetch();
    }

    @Override
    public List<ClassDto> findAllByKinderNo(Long kinderNo) {
        List<ClassDto> classes = jpaQueryFactory
                .select(Projections.constructor(ClassDto.class,
                        clazz.classNo,
                        clazz.className,
                        clazz.kinderNo
                ))
                .from(clazz)
                .where(clazz.kinderNo.eq(kinderNo),
                        clazz.classDeleteFlag.eq(BooleanEnum.FALSE.getBool()))
                .fetch();
        return classes;
    }

    @Override
    public List<ClassDto> findAddableClassByKinderNo(Long kinderNo) {
        List<ClassDto> classes = jpaQueryFactory
                .select(Projections.constructor(ClassDto.class,
                        clazz.classNo,
                        clazz.className,
                        clazz.kinderNo,
                        classTeacher.classTeacherId.count().as("assignedCnt")
                ))
                .from(clazz)
                .join(classTeacher).on(clazz.classNo.eq(classTeacher.classNo))
                .join(acceptLog).on(classTeacher.acceptNo.eq(acceptLog.acceptNo))
                .where(clazz.kinderNo.eq(kinderNo),
                        clazz.classDeleteFlag.eq(BooleanEnum.FALSE.getBool()),
                        acceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()))
                .groupBy(clazz.classNo,
                        clazz.className)
                .having(classTeacher.classTeacherId.count().lt(3L))
                .fetch();
        return classes;

    }

    @Override
    public List<ClassDto> findClassByKinderNoAndTeacherId(Long kinderNo, Long id) {
        List<ClassDto> classes = jpaQueryFactory
                .select(Projections.constructor(ClassDto.class,
                        clazz.classNo,
                        clazz.className,
                        clazz.kinderNo,
                        acceptLog.acceptNo))
                .from(clazz)
                .join(classTeacher).on(clazz.classNo.eq(classTeacher.classNo))
                .join(acceptLog).on(classTeacher.acceptNo.eq(acceptLog.acceptNo))
                .where(clazz.kinderNo.eq(kinderNo),
                        clazz.classDeleteFlag.eq(BooleanEnum.FALSE.getBool()),
                        acceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        classTeacher.classTeacherId.eq(id))
                .fetch();

        return classes;
    }
}
