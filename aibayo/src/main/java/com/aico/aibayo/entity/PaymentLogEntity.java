package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_log_no")
    private Long paymentLogNo;
    @Column(name = "bill_no")
    private Long billNo;
    @Column(name = "payment_status")
    private Integer paymentStatus;
    @Column(name = "payment_log_reg_date")
    private LocalDateTime paymentLogRegDate;
}
