package com.aico.aibayo.repository.member;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.MemberStatusEnum;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.entity.QAcceptLogEntity;
import com.aico.aibayo.entity.QMemberEntity;
import com.aico.aibayo.entity.QParentKidEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QParentKidEntity parentKid = QParentKidEntity.parentKidEntity;
    private final QAcceptLogEntity acceptLog1 = QAcceptLogEntity.acceptLogEntity;

    public List<MemberDto> findAllByKidNo(Long kidNo) {
        return jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.email,
                        member.name,
                        member.pw,
                        member.phone,
                        member.regType,
                        member.roleNo,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.latestIp,
                        member.profilePicture,
                        member.kinderNo
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
    public MemberDto findByIdAndKidNo(MemberSearchCondition condition) {
        return jpaQueryFactory
                .select(Projections.constructor(MemberDto.class,
                        member.id,
                        member.email,
                        member.name,
                        member.pw,
                        member.phone,
                        member.regType,
                        member.roleNo,
                        member.status,
                        member.regDate,
                        member.modifyDate,
                        member.inactivateDate,
                        member.latestLogDate,
                        member.latestIp,
                        member.profilePicture,
                        member.kinderNo,
                        parentKid.isMainParent
                        ))
                .from(member)
                .join(parentKid).on(member.id.eq(parentKid.id))
                .join(acceptLog1).on(acceptLog1.acceptNo.eq(parentKid.acceptNo))
                .where(
                        acceptLog1.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        member.id.eq(condition.getId()),
                        parentKid.kidNo.eq(condition.getKidNo())
                )
                .fetchOne();
    }
}