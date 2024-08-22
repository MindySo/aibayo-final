package com.aico.aibayo.repository.payment;

import com.aico.aibayo.entity.PaymentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLogEntity, Long>, PaymentLogRepositoryCustom {
}
