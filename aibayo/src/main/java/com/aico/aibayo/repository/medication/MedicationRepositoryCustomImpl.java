package com.aico.aibayo.repository.medication;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.medication.MedicationDto;
import com.aico.aibayo.dto.medication.MedicationSearchCondition;
import com.aico.aibayo.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class MedicationRepositoryCustomImpl implements MedicationRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final QMemberEntity member =QMemberEntity.memberEntity;
    private final QAcceptLogEntity acceptLog=QAcceptLogEntity.acceptLogEntity;
    private final QMedicationDetailEntity medicationDetail=QMedicationDetailEntity.medicationDetailEntity;
    private final QOrderFormEntity orderForm=QOrderFormEntity.orderFormEntity;
    private final QMedicationOrderEntity medicationOrder=QMedicationOrderEntity.medicationOrderEntity;
    private final QMedicationPicEntity medicationPic=QMedicationPicEntity.medicationPicEntity;
    private final QMedicationReportEntity medicationReport=QMedicationReportEntity.medicationReportEntity;
    private final QKidEntity kid = QKidEntity.kidEntity;



    @Override
    public Page<MedicationDto> finAllByKinderNoList(MedicationSearchCondition condition, Pageable pageable) {
        List<MedicationDto>mediOrders=jpaQueryFactory
                .select(Projections.constructor(MedicationDto.class,
                        kid.kidName,
                        kid.kidNo,
                        orderForm.orderRequester,
                        orderForm.orderRequestDate,
                        orderForm.runDate,
                        orderForm.orderNo,
                        medicationOrder.symptoms))
                .from(orderForm)
                .join(kid).on(orderForm.kidNo.eq(kid.kidNo))
                .join(medicationOrder).on(orderForm.orderNo.eq(orderForm.orderNo))
                .where(
                        getOrderDeleteFlag(orderForm),
                        getDischargeFlag(kid),
                        getKinderNoEq(condition.getKinderNo(), kid),
                        getOrderRequestDate(condition.getOrderRequestDate(), orderForm),
                        getOrderTypeEq(condition.getOrderType(), orderForm)
                        )
                .orderBy(orderForm.runDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(orderForm.count())
                .from(orderForm)
                .join(kid).on(orderForm.kidNo.eq(kid.kidNo))
                .join(medicationOrder).on(orderForm.orderNo.eq(orderForm.orderNo))
                .where(
                        getOrderDeleteFlag(orderForm),
                        getDischargeFlag(kid),
                        getKinderNoEq(condition.getKinderNo(), kid),
                        getOrderRequestDate(condition.getOrderRequestDate(), orderForm),
                        getOrderTypeEq(condition.getOrderType(), orderForm)
                );

        return PageableExecutionUtils.getPage(mediOrders, pageable, count::fetchOne);
    }

    @Override
    public Page<MedicationDto> findAllByKinderNoCard(MedicationSearchCondition condition, Pageable pageable) {
        return null;
    }

    @Override
    public MedicationDto findByOrderNo(Long orderNo) {
        return null;
    }
    private BooleanExpression getOrderTypeEq(Integer orderType, QOrderFormEntity orderForm) {
        return orderType == null ? null : orderForm.orderType.eq(orderType);
    }

    private BooleanExpression getKinderNoEq(Long kinderNo, QKidEntity kid) {
        return kinderNo == null ? null : member.kinderNo.eq(kinderNo);
    }
    private BooleanExpression getOrderDeleteFlag(QOrderFormEntity orderForm) {
        return orderForm.orderDeleteFlag.eq(BooleanEnum.FALSE.getBool());
    }
    private BooleanExpression getDischargeFlag(QKidEntity kid) {
        return kid.dischargeFlag.eq(BooleanEnum.FALSE.getBool());
    }
    private BooleanExpression getOrderRequestDate(LocalDateTime orderRequestDate, QOrderFormEntity orderForm) {
        if (orderRequestDate == null) {
            return null;
        }

        LocalDate date = orderRequestDate.toLocalDate();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX).minusSeconds(1L);

        return orderForm.orderRequestDate.between(startDateTime, endDateTime);
    }


}
