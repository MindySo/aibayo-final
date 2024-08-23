package com.aico.aibayo.repository.teacher;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.AcceptTypeEnum;
import com.aico.aibayo.common.InviteTypeEnum;
import com.aico.aibayo.dto.teacher.TeacherSearchCondition;
import com.aico.aibayo.dto.teacher.TeacherDto;
import com.aico.aibayo.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TeacherRepositoryCustomImpl implements TeacherRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QClassEntity classEntity = QClassEntity.classEntity;
    private final QTeacherKinderEntity teacherKinder = QTeacherKinderEntity.teacherKinderEntity;
    private final QClassTeacherEntity classTeacher = QClassTeacherEntity.classTeacherEntity;
    private final QInviteCodeEntity inviteCode = QInviteCodeEntity.inviteCodeEntity;
    private final QAcceptLogEntity kinderAcceptLog = QAcceptLogEntity.acceptLogEntity;
    private final QAcceptLogEntity classAcceptLog = QAcceptLogEntity.acceptLogEntity;

    @Override
    public List<TeacherDto> findAcceptedTeacherByKinderNoAndClassNo(TeacherSearchCondition condition) {
        List<TeacherDto> teachers = jpaQueryFactory
                .select(Projections.constructor(TeacherDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.phone,
                        member.profilePicture,
                        kinderAcceptLog.acceptRegDate,
                        teacherKinder.acceptNo)).distinct()
                .from(member)
                .join(teacherKinder).on(member.id.eq(teacherKinder.teacherId))
                .join(kinderAcceptLog).on(teacherKinder.acceptNo.eq(kinderAcceptLog.acceptNo))
                .join(classTeacher).on(member.id.eq(classTeacher.classTeacherId))
                .join(classAcceptLog).on(classTeacher.acceptNo.eq(classAcceptLog.acceptNo))
                .where(kinderAcceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        classAcceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        teacherKinder.kinderNo.eq(condition.getKinderNo()),
                        getClassNoEq(condition.getClassNo())
                )
                .fetch();
        return teachers;
    }

    @Override
    public List<TeacherDto> findTeacherByKinderNo(TeacherSearchCondition condition) {
        List<TeacherDto> teachers = jpaQueryFactory
                .select(Projections.constructor(TeacherDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.phone,
                        member.profilePicture,
                        kinderAcceptLog.acceptRegDate,
                        kinderAcceptLog.acceptNo)).distinct()
                .from(member)
                .join(teacherKinder).on(member.id.eq(teacherKinder.teacherId))
                .join(kinderAcceptLog).on(teacherKinder.acceptNo.eq(kinderAcceptLog.acceptNo))
                .where(
                        kinderAcceptLog.acceptStatus.eq(condition.getAcceptStatus()),
                        teacherKinder.kinderNo.eq(condition.getKinderNo())
                )
                .fetch();
        System.out.println(teachers.toString());
        return teachers;
    }

    @Override
    public List<TeacherDto> findInvitedTeacherByKinderNo(TeacherSearchCondition condition) {
        List<TeacherDto> teachers = jpaQueryFactory
                .select(Projections.constructor(TeacherDto.class,
                        inviteCode.inviteEmail,
                        inviteCode.acceptNo)).distinct()
                .from(kinderAcceptLog)
                .join(inviteCode).on(kinderAcceptLog.acceptNo.eq(inviteCode.acceptNo))
                .where(
                        kinderAcceptLog.acceptType.eq(AcceptTypeEnum.INVITE_CODE.getType()),
                        inviteCode.kinderNo.eq(condition.getKinderNo()),
                        inviteCode.inviteType.eq(InviteTypeEnum.TEACHER.getType())
                )
                .fetch();
        System.out.println(teachers.toString());
        return teachers;
    }

    @Override
    public TeacherDto findTeacherById(Long id) {
        TeacherDto teacher = jpaQueryFactory
                .select(Projections.constructor(TeacherDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.phone,
                        member.profilePicture,
                        kinderAcceptLog.acceptRegDate,
                        teacherKinder.acceptNo,
                        member.kinderNo
                        ))
                .from(member)
                .join(teacherKinder).on(member.id.eq(teacherKinder.teacherId))
                .join(kinderAcceptLog).on(teacherKinder.acceptNo.eq(kinderAcceptLog.acceptNo))
                .where(kinderAcceptLog.acceptStatus.eq(1),
                        member.id.eq(id)
                ).fetchOne();

        return teacher;
    }

    private BooleanExpression getClassNoEq(Long classNo) {
        return classNo == null ? null : classTeacher.classNo.eq(classNo);
    }
}