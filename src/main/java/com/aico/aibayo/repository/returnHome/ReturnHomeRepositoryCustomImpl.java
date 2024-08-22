package com.aico.aibayo.repository.returnHome;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.returnHome.ReturnHomeDto;
import com.aico.aibayo.dto.returnHome.ReturnHomeSearchCondition;
import com.aico.aibayo.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ReturnHomeRepositoryCustomImpl implements ReturnHomeRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final QOrderFormEntity orderForm = QOrderFormEntity.orderFormEntity;
    private final QKidEntity kid = QKidEntity.kidEntity;
    private final QReturnHomeAgreementEntity returnHomeAgreement = QReturnHomeAgreementEntity.returnHomeAgreementEntity;
    private final QReturnHomeAgreementParentEntity returnHomeAgreementParent = QReturnHomeAgreementParentEntity.returnHomeAgreementParentEntity;

    @Override
    public Page<ReturnHomeDto> findAllByKinderNo(ReturnHomeSearchCondition condition, Pageable pageable) {
        List<ReturnHomeDto> Homeorder = jpaQueryFactory
                .select(Projections.constructor(ReturnHomeDto.class,
                        kid.kidName,
                        returnHomeAgreement.rhType,
                        orderForm.runDate,
                        returnHomeAgreement.rhTime,
                        orderForm.orderRequester,
                        orderForm.orderRequestDate,
                        orderForm.orderNo,
                        orderForm.orderType,
                        kid.kidNo,
                        orderForm.orderDeleteFlag,
                        returnHomeAgreement.rhAgreeNo,
                        kid.dischargeFlag
                ))
                .from(orderForm)
                .join(kid).on(orderForm.kidNo.eq(kid.kidNo))
                .join(returnHomeAgreement).on(orderForm.orderNo.eq(returnHomeAgreement.orderNo))
                .join(returnHomeAgreementParent).on(returnHomeAgreement.rhAgreeNo.eq(returnHomeAgreementParent.rhAgreeNo))
                .where(
                    getDischargeFlagEq(kid),
                    getOrderTypeEq(condition.getOrderNo(),orderForm),
                    getKinderNoEq(condition.getKinderNo(), kid)
                )
                .orderBy(orderForm.runDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory.select(orderForm.count())
                .from(orderForm)
                .join(kid).on(orderForm.kidNo.eq(kid.kidNo))
                .join(returnHomeAgreement).on(orderForm.orderNo.eq(returnHomeAgreement.orderNo))
                .join(returnHomeAgreementParent).on(returnHomeAgreement.rhAgreeNo.eq(returnHomeAgreementParent.rhAgreeNo))
                .where(
                        getDischargeFlagEq(kid),
                        getOrderTypeEq(condition.getOrderNo(), orderForm),
                        getKinderNoEq(condition.getKinderNo(), kid)
                );
        return PageableExecutionUtils.getPage(Homeorder, pageable, count::fetchOne);
    }

    @Override
    public Page<ReturnHomeDto> findAllByKidNo(ReturnHomeSearchCondition condition, Pageable pageable) {
        List<ReturnHomeDto> Homeorder = jpaQueryFactory
                .select(Projections.constructor(ReturnHomeDto.class,
                        kid.kidName,
                        returnHomeAgreement.rhType,
                        orderForm.runDate,
                        returnHomeAgreement.rhTime,
                        orderForm.orderRequester,
                        orderForm.orderRequestDate,
                        orderForm.orderNo,
                        orderForm.orderType,
                        kid.kidNo,
                        orderForm.orderDeleteFlag,
                        returnHomeAgreement.rhAgreeNo,
                        kid.dischargeFlag
                ))
                .from(orderForm)
                .join(kid).on(orderForm.kidNo.eq(kid.kidNo))
                .join(returnHomeAgreement).on(orderForm.orderNo.eq(returnHomeAgreement.orderNo))
                .join(returnHomeAgreementParent).on(returnHomeAgreement.rhAgreeNo.eq(returnHomeAgreementParent.rhAgreeNo))
                .where(
                        getDischargeFlagEq(kid),
                        getOrderTypeEq(condition.getOrderNo(),orderForm),
                        getKinderNoEq(condition.getKinderNo(), kid),
                        getKidNoEq(condition.getKidNo(),kid)
                )
                .orderBy(orderForm.runDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> count = jpaQueryFactory.select(orderForm.count())
                .from(orderForm)
                .join(kid).on(orderForm.kidNo.eq(kid.kidNo))
                .join(returnHomeAgreement).on(orderForm.orderNo.eq(returnHomeAgreement.orderNo))
                .join(returnHomeAgreementParent).on(returnHomeAgreement.rhAgreeNo.eq(returnHomeAgreementParent.rhAgreeNo))
                .where(
                        getDischargeFlagEq(kid),
                        getOrderTypeEq(condition.getOrderNo(), orderForm),
                        getKinderNoEq(condition.getKinderNo(), kid),
                        getKidNoEq(condition.getKidNo(),kid)
                );
        return PageableExecutionUtils.getPage(Homeorder, pageable, count::fetchOne);
    }
    @Override
    public ReturnHomeDto findByRhAgreeNo(Long rhAgreeNo) {
        return null;
    }

    private BooleanExpression getDischargeFlagEq(QKidEntity kid) {
        return kid.dischargeFlag.eq(BooleanEnum.FALSE.getBool());
    }

    private BooleanExpression getOrderTypeEq(Long orderNo, QOrderFormEntity orderForm) {
        return orderNo == null ? null :
                orderForm.orderNo.eq(orderNo);
    }

    private BooleanExpression getKinderNoEq(Long kinderNo, QKidEntity kid) {
        return kinderNo == null ? null : kid.kinderNo.eq(kinderNo);
    }

    private BooleanExpression getKidNoEq(Long kidNo, QKidEntity kid) {
        return kidNo == null ? null : kid.kidNo.eq(kidNo);
    }

}
