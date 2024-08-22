package com.aico.aibayo.repository.member;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.MemberStatusEnum;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QParentKidEntity parentKid = QParentKidEntity.parentKidEntity;
    private final QClassTeacherEntity classTeacher = QClassTeacherEntity.classTeacherEntity;
    private final QAcceptLogEntity acceptLog1 = QAcceptLogEntity.acceptLogEntity;
    private final QKidEntity kid = QKidEntity.kidEntity;


    public List<MemberDto> findAllByKidNo(Long kidNo) {
        return jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.password,
                        member.phone,
                        member.roleNo,
                        member.role,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.profilePicture,
                        member.kinderNo,
                        acceptLog1.acceptNo
                        ))
                .from(member)
                .join(parentKid).on(member.id.eq(parentKid.id))
                .join(acceptLog1).on(acceptLog1.acceptNo.eq(parentKid.acceptNo))
                .where(
                        member.status.eq(MemberStatusEnum.ACTIVE.getStatus()),
                        acceptLog1.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        parentKid.kidNo.eq(kidNo)
                )
                .fetch();
    }

    @Override
    public Optional<MemberDto> findByIdAndKidNo(MemberSearchCondition condition) {
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.password,
                        member.phone,
                        member.roleNo,
                        member.role,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.profilePicture,
                        kid.kinderNo,
                        parentKid.isMainParent
                        ))
                .from(member)
                .join(parentKid).on(member.id.eq(parentKid.id))
                .join(acceptLog1).on(acceptLog1.acceptNo.eq(parentKid.acceptNo))
                .join(kid).on(parentKid.kidNo.eq(kid.kidNo))
                .where(
                        acceptLog1.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        member.id.eq(condition.getId()),
                        parentKid.kidNo.eq(condition.getKidNo())
                )
                .fetchOne();
        return Optional.ofNullable(memberDto);
    }

    @Override
    public Optional<MemberDto> findByUsernameWithParentKid(String username) {
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.password,
                        member.phone,
                        member.roleNo,
                        member.role,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.profilePicture,
                        kid.kinderNo,
                        kid.kidNo,
                        acceptLog1.acceptNo,
                        acceptLog1.acceptStatus,
                        parentKid.isMainParent
                ))
                .from(member)
                .join(parentKid).on(member.id.eq(parentKid.id))
                .join(kid).on(
                        kid.kidNo.eq(parentKid.kidNo)
                        .and(kid.dischargeFlag.eq(BooleanEnum.FALSE.getBool()))
                )
                .join(acceptLog1).on(
                        acceptLog1.acceptNo.eq(parentKid.acceptNo)
                        .and(acceptLog1.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()))
                )
                .where(
                        member.status.eq(MemberStatusEnum.ACTIVE.getStatus()),
                        member.username.eq(username)
                )
                .orderBy(acceptLog1.acceptDate.asc())
                .fetchFirst();
        return Optional.ofNullable(memberDto);
    }

    @Override
    public Optional<MemberDto> findByUsernameWithParentKid(MemberSearchCondition condition) {
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.password,
                        member.phone,
                        member.roleNo,
                        member.role,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.profilePicture,
                        kid.kinderNo,
                        kid.kidNo,
                        acceptLog1.acceptNo,
                        acceptLog1.acceptStatus,
                        parentKid.isMainParent
                ))
                .from(member)
                .join(parentKid).on(member.id.eq(parentKid.id))
                .join(kid).on(
                        kid.kidNo.eq(parentKid.kidNo)
                                .and(kid.dischargeFlag.eq(BooleanEnum.FALSE.getBool()))
                )
                .join(acceptLog1).on(
                        acceptLog1.acceptNo.eq(parentKid.acceptNo)
                                .and(acceptLog1.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()))
                )
                .where(
                        member.status.eq(MemberStatusEnum.ACTIVE.getStatus()),
                        member.username.eq(condition.getUsername()),
                        getKidNoEq(condition.getKidNo())
                )
                .orderBy(acceptLog1.acceptDate.asc())
                .fetchFirst();
        return Optional.ofNullable(memberDto);
    }

    private BooleanExpression getKidNoEq(Long kidNo) {
        return kidNo == null ? null : kid.kidNo.eq(kidNo);
    }


    @Override
    public Optional<MemberDto> findByUsernameWithClassTeacher(String username) {
        MemberDto memberDto = jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.username,
                        member.name,
                        member.password,
                        member.phone,
                        member.roleNo,
                        member.role,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.profilePicture,
                        acceptLog1.acceptNo,
                        acceptLog1.acceptStatus
                        ))
                .from(member)
                .join(classTeacher).on(member.id.eq(classTeacher.classTeacherId))
                .join(acceptLog1).on(acceptLog1.acceptNo.eq(classTeacher.acceptNo)
                        .and(acceptLog1.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus())))
                .where(member.status.eq(MemberStatusEnum.ACTIVE.getStatus()),
                        member.username.eq(username))
                .fetchFirst();
        return Optional.ofNullable(memberDto);
    }

    @Override
    public MemberDto findDtoById(Long id) {
        return jpaQueryFactory
        .select(Projections.constructor(MemberDto.class,
                member.id,
                member.username,
                member.name,
                member.password,
                member.phone,
                member.roleNo,
                member.role,
                member.status,
                member.kinderNo
        ))
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

}
