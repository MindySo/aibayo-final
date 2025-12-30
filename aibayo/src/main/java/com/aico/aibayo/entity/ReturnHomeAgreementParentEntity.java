package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "return_home_agreement_parent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReturnHomeAgreementParentEntity {
    @Id
    @Column(name="rh_agree_parent_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rhAgreeParentNo; //귀가 동의 보호자 번호
    @Column(name="rh_agree_no")
    private Long rhAgreeNo; //귀가 동의 번호
    @Column(name="rh_main_parent_name")
    private String rhMainParentName; //귀가 동의 주 보호자명
    @Column(name="rh_main_parent_tel")
    private String rhMainParentTel; //귀가 동의 주 보호자 연락처
    @Column(name="rh_minor_parent_name")
    private String rhMinorParentName; //귀가 동의 부 보호자명
    @Column(name="rh_minor_parent_tel")
    private String rhMinorParentTel; //귀가 동의 부 보호자 연락처
}
