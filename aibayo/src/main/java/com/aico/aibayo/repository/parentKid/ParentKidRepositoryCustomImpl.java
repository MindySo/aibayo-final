package com.aico.aibayo.repository.parentKid;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.ParentKidDto;
import com.aico.aibayo.entity.QAcceptLogEntity;
import com.aico.aibayo.entity.QParentKidEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParentKidRepositoryCustomImpl implements ParentKidRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private final QParentKidEntity parentKid = QParentKidEntity.parentKidEntity;
    private final QAcceptLogEntity acceptLog = QAcceptLogEntity.acceptLogEntity;

    @Override
    public ParentKidDto findByKidMainParent(Long kidNo) {
        return jpaQueryFactory
                .select(Projections.constructor(ParentKidDto.class,
                        parentKid.id,
                        parentKid.kidNo,
                        parentKid.acceptNo,
                        parentKid.isMainParent,
                        parentKid.parentRelationship
                        ))
                .from(parentKid)
                .join(acceptLog).on(acceptLog.acceptNo.eq(parentKid.acceptNo))
                .where(
                        acceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus()),
                        parentKid.kidNo.eq(kidNo),
                        parentKid.isMainParent.eq(BooleanEnum.TRUE.getBool())
                )
                .fetchOne();
    }
}
