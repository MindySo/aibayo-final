package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medication_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class MedicationOrderEntity {
    @Id
    @Column(name="medi_order_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediOrderNo; //투약 의뢰 번호
    @Column(name="order_no")
    private Long orderNo; //의뢰 번호
    @Column
    private String symptoms; //증상
}
