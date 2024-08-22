package com.aico.aibayo.repository.payment;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.aico.aibayo.common.PaymentStatusEnum;
import com.aico.aibayo.dto.payment.PaymentDto;
import com.aico.aibayo.dto.payment.PaymentSearchCondition;
import com.aico.aibayo.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private final QPaymentEntity payment = QPaymentEntity.paymentEntity;
    private final QPaymentLogEntity paymentLog = QPaymentLogEntity.paymentLogEntity;
    private final QKidEntity kid = QKidEntity.kidEntity;
    private final QClassEntity clazz = QClassEntity.classEntity;
    private final QMemberEntity member = QMemberEntity.memberEntity;
    private final QParentKidEntity parentKid = QParentKidEntity.parentKidEntity;

    @Override
    public List<PaymentDto> findAllByKinderNo(PaymentSearchCondition condition) {
        List<PaymentDto> paymentDtoList = jpaQueryFactory
                .select(Projections.constructor(PaymentDto.class,
                        payment.billNo,
                        payment.kidNo,
                        payment.classNo,
                        payment.discountRate,
                        payment.paymentTitle,
                        payment.paymentPrice,
                        payment.paymentStartDate,
                        payment.paymentEndDate,
                        payment.paymentMemo,
                        paymentLog.paymentStatus,
                        paymentLog.paymentLogRegDate,
                        kid.kidName,
                        clazz.className)).distinct()
                .from(payment)
                .join(paymentLog).on(payment.billNo.eq(paymentLog.billNo))
                .join(kid).on(payment.kidNo.eq(kid.kidNo))
                .join(clazz).on(payment.classNo.eq(clazz.classNo))
                .join(parentKid).on(payment.kidNo.eq(parentKid.kidNo))
                .where(payment.kinderNo.eq(condition.getKinderNo()))
                .fetch();

        return paymentDtoList;
    }

    @Override
    public List<PaymentDto> findAllBySearchCondition(PaymentSearchCondition condition) {
        List<PaymentDto> paymentDtoList = jpaQueryFactory
                .select(Projections.constructor(PaymentDto.class,
                        payment.billNo,
                        payment.kidNo,
                        payment.classNo,
                        payment.discountRate,
                        payment.paymentTitle,
                        payment.paymentPrice,
                        payment.paymentStartDate,
                        payment.paymentEndDate,
                        payment.paymentMemo,
                        paymentLog.paymentStatus,
                        paymentLog.paymentLogRegDate,
                        kid.kidName,
                        clazz.className))
                .from(payment)
                .join(paymentLog).on(payment.billNo.eq(paymentLog.billNo))
                .join(kid).on(payment.kidNo.eq(kid.kidNo))
                .join(clazz).on(payment.classNo.eq(clazz.classNo))
                .where(payment.kinderNo.eq(condition.getKinderNo()))
                .where(getPaymentLogRegdateGoe(condition.getStartDate()),
                        getPaymentLogRegdateLoe(condition.getEndDate()),
                        getPaymentStatusIn(condition.getPaymentStatusList()),
                        getStringLike(condition.getInputString()),
                        getClassNoEq(condition.getClassNo()))
                .fetch();

        return paymentDtoList;
    }

    @Override
    public List<PaymentDto> findAllByMemberId(PaymentSearchCondition condition) {
        List<PaymentDto> paymentDtoList = jpaQueryFactory
                .select(Projections.constructor(PaymentDto.class,
                        payment.billNo,
                        payment.kidNo,
                        payment.classNo,
                        payment.discountRate,
                        payment.paymentTitle,
                        payment.paymentPrice,
                        payment.paymentStartDate,
                        payment.paymentEndDate,
                        payment.paymentMemo,
                        paymentLog.paymentStatus,
                        paymentLog.paymentLogRegDate,
                        kid.kidName,
                        parentKid.id,
                        clazz.className))
                .from(payment)
                .join(paymentLog).on(payment.billNo.eq(paymentLog.billNo))
                .join(kid).on(payment.kidNo.eq(kid.kidNo))
                .join(clazz).on(payment.classNo.eq(clazz.classNo))
                .join(parentKid).on(payment.kidNo.eq(parentKid.kidNo))
                .where(parentKid.id.eq(condition.getMemberId()))
                .fetch();

        return paymentDtoList;
    }

    @Override
    public PaymentDto findByBillNo(PaymentSearchCondition condition) {
        PaymentDto paymentDto = jpaQueryFactory
                .select(Projections.constructor(PaymentDto.class,
                        payment.billNo,
                        payment.kidNo,
                        payment.classNo,
                        payment.discountRate,
                        payment.paymentTitle,
                        payment.paymentPrice,
                        payment.paymentStartDate,
                        payment.paymentEndDate,
                        payment.paymentMemo,
                        paymentLog.paymentStatus,
                        paymentLog.paymentLogRegDate,
                        kid.kidName,
                        clazz.className)).distinct()
                .from(payment)
                .join(paymentLog).on(payment.billNo.eq(paymentLog.billNo))
                .join(kid).on(payment.kidNo.eq(kid.kidNo))
                .join(clazz).on(payment.classNo.eq(clazz.classNo))
                .join(parentKid).on(payment.kidNo.eq(parentKid.kidNo))
                .where(payment.billNo.eq(condition.getBillNo()),
                        paymentLog.paymentStatus.eq(PaymentStatusEnum.BILLED.getStatus()))
                .fetchOne();


        return paymentDto;
    }

    private BooleanExpression getPaymentLogRegdateGoe(LocalDateTime startDate) {
        if (startDate == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return paymentLog.paymentLogRegDate.goe(startDate);
        }
    }

    private BooleanExpression getPaymentLogRegdateLoe(LocalDateTime endDate) {
        if (endDate == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return paymentLog.paymentLogRegDate.goe(endDate);
        }
    }

    private BooleanExpression getPaymentStatusIn(List<Integer> paymentStatusList) {
        if (paymentStatusList == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return paymentLog.paymentStatus.in(paymentStatusList);
        }
    }


    private BooleanExpression getStringLike(String inputString) {
        if (inputString == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return getPaymentTitleLike(inputString).or(getKidNameLike(inputString));
        }
    }

    private BooleanExpression getPaymentTitleLike(String inputString) {
        if (inputString == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return payment.paymentTitle.like(inputString);
        }
    }

    private BooleanExpression getKidNameLike(String inputString) {
        if (inputString == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return kid.kidName.like(inputString);
        }
    }

    private BooleanExpression getClassNoEq(Long classNo) {
        if (classNo == null) {
            return null; // null인 경우 전체 조건을 무시
        } else {
            return clazz.classNo.eq(classNo);
        }
    }
}
