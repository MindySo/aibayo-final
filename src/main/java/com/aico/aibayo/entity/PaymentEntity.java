package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_no")
    private Long billNo;
    @Column(name = "id")
    private Long id;
    @Column(name = "kid_no")
    private Long kidNo;
    @Column(name = "class_no")
    private Long classNo;
    @Column(name = "kinder_no")
    private Long kinderNo;
    @Column(name = "discount_rate")
    private Long discountRate;
    @Column(name = "payment_title")
    private String paymentTitle;
    @Column(name = "payment_price")
    private Long paymentPrice;
    @Column(name = "payment_start_date")
    private LocalDateTime paymentStartDate;
    @Column(name = "payment_end_date")
    private LocalDateTime paymentEndDate;
    @Column(name = "payment_memo")
    private String paymentMemo;
}
