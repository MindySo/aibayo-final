package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medication_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class MedicationDetailEntity {
    @Id
    @Column(name="medi_detail_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mediDetailNo; //투약 내용 번호
    @Column(name="medi_order_no")
    private Long mediOrderNo; //투약 의뢰번호
    @Column(name="medi_type")
    private String mediType; //약 종류
    @Column
    private String dosage; //용량
    @Column(name="dose_count")
    private String doseCount; //투약 횟수
    @Column(name="dose_time")
    private String doseTime; //투약 시간
    @Column(name="storage_type")
    private String storageType; //보관 방법 0: 실온 1:냉장
}
