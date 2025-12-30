package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "kid")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KidEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kid_no")
    private Long kidNo;
    @Column(name = "kinder_no")
    private Long kinderNo;
    @Column(name = "kid_name")
    private String kidName;
    @Column(name = "kid_birth")
    private LocalDate kidBirth;
    @Column(name = "kid_gender")
    private Integer kidGender;
    @Column(name = "admission_date")
    private LocalDateTime admissionDate;
    @Column(name = "modify_date")
    private LocalDateTime modifyDate;
    @Column(name = "discharge_date")
    private LocalDateTime dischargeDate;
    @Column(name = "discharge_flag")
    private String dischargeFlag;
}
