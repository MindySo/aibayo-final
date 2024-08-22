package com.aico.aibayo.service.payment;

import com.aico.aibayo.common.BoardTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.PaymentStatusEnum;
import com.aico.aibayo.dto.payment.PaymentDto;
import com.aico.aibayo.dto.payment.PaymentSearchCondition;
import com.aico.aibayo.entity.*;
import com.aico.aibayo.repository.payment.PaymentLogRepository;
import com.aico.aibayo.repository.payment.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentLogRepository paymentLogRepository;

    @Override
    public List<PaymentDto> getAllByKinderNo(PaymentSearchCondition condition) {
        return paymentRepository.findAllByKinderNo(condition);
    }

    @Override
    public List<PaymentDto> getAllBySearchCondition(PaymentSearchCondition condition) {
        return paymentRepository.findAllBySearchCondition(condition);
    }

    @Override
    @Transactional
    public void insertPayment(Map<String, Object> requestBody) {
        List<Map<String, Object>> paymentList = (List<Map<String, Object>>) requestBody.get("billList");

//        for(PaymentDto payment : paymentList) {
        for (Map<String, Object> paymentMap : paymentList) {
            PaymentDto payment =
//                    convertMapToPaymentDto(paymentMap);

            PaymentDto.builder()
                    .id(Long.valueOf(paymentMap.get("id").toString()))
                    .kidNo(Long.valueOf(paymentMap.get("kidNo").toString()))
                    .classNo(Long.valueOf(paymentMap.get("classNo").toString()))
                    .discountRate(Long.valueOf(paymentMap.get("discountRate").toString()))
                    .kinderNo(Long.valueOf(paymentMap.get("kinderNo").toString()))
                    .paymentTitle(paymentMap.get("paymentTitle").toString())
                    .paymentPrice(Long.valueOf(paymentMap.get("paymentPrice").toString()))
                    .paymentEndDate(LocalDateTime.parse(paymentMap.get("paymentEndDate").toString()))
                    .paymentMemo(paymentMap.get("paymentMemo").toString())
                    .build();


            // payment insert
            PaymentEntity paymentEntity= PaymentEntity.builder()
                    .id(payment.getId())
                    .kidNo(payment.getKidNo())
                    .classNo(payment.getClassNo())
                    .discountRate(payment.getDiscountRate())
                    .kinderNo(payment.getKinderNo())
                    .paymentStartDate(LocalDateTime.now())
                    .paymentEndDate(payment.getPaymentEndDate())
                    .paymentPrice(payment.getPaymentPrice())
                    .discountRate(payment.getDiscountRate())
                    .paymentTitle(payment.getPaymentTitle())
                    .paymentMemo(payment.getPaymentMemo())
                    .build();

            PaymentEntity savedPayment = paymentRepository.save(paymentEntity);

            // paymentLog insert
            PaymentLogEntity paymentLogEntity = PaymentLogEntity.builder()
                    .billNo(savedPayment.getBillNo())
                    .paymentStatus(PaymentStatusEnum.BILLED.getStatus())
                    .paymentLogRegDate(savedPayment.getPaymentStartDate())
                    .build();

            paymentLogRepository.save(paymentLogEntity);
        }
    }

    @Override
    public List<PaymentDto> getAllByMemberId(PaymentSearchCondition condition) {
        return paymentRepository.findAllByMemberId(condition);
    }

    @Override
    public PaymentDto getByBillNo(PaymentSearchCondition condition) {
        return paymentRepository.findByBillNo(condition);
    }

    @Override
    public void insertPaymentSuccess(PaymentSearchCondition condition) {

        PaymentDto payment = paymentRepository.findByBillNo(condition);

        // paymentLog insert
        PaymentLogEntity paymentLogEntity = PaymentLogEntity.builder()
                .billNo(payment.getBillNo())
                .paymentStatus(PaymentStatusEnum.PAID.getStatus())
                .paymentLogRegDate(LocalDateTime.now())
                .build();

        paymentLogRepository.save(paymentLogEntity);
    }
}
