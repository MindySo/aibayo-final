package com.aico.aibayo.repository.payment;

import com.aico.aibayo.dto.payment.PaymentDto;
import com.aico.aibayo.dto.payment.PaymentSearchCondition;

import java.util.List;

public interface PaymentRepositoryCustom {
    List<PaymentDto> findAllByKinderNo(PaymentSearchCondition condition);
    List<PaymentDto> findAllBySearchCondition(PaymentSearchCondition condition);
    List<PaymentDto> findAllByMemberId(PaymentSearchCondition condition);
    PaymentDto findByBillNo(PaymentSearchCondition condition);
}
