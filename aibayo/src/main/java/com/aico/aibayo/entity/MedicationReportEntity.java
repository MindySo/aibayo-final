package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medication_report")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class MedicationReportEntity {
    @Id
    @Column(name="medi_order_no")
    private Long mediOrderNo; //투약 의뢰 번호
    @Column(name="medi_report_specific")
    private String mediReportSpecific; //투약 보고 특이사항
}
