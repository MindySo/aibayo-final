package com.aico.aibayo.repository.payment;

import com.aico.aibayo.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>, PaymentRepositoryCustom {
}
