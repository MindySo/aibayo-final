package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_form")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class OrderFormEntity {
    @Id
    @Column(name="order_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNo; //의뢰 번호
    @Column(name="order_type")
    private Integer orderType; //의뢰 분류 0: 투약 1: 귀가
    @Column(name="order_requester")
    private String orderRequester; //의뢰자
    @Column(name="order_request_date")
    private LocalDateTime orderRequestDate; //의뢰일자 (작성일)
    @Column(name="run_date")
    private LocalDateTime runDate; //실행일자
    @Column(name="kid_no")
    private Long kidNo; //원생번호
    @Column(name="order_checked")
    private Integer orderChecked; //의뢰확인여부
    @Column(name="order_specific")
    private String orderSpecific; //특이사항
    @Column(name="order_parent_sign")
    private String orderParentSign; //보호자 서명
    @Column(name="order_delete_date")
    private LocalDateTime orderDeleteDate; //의뢰 삭제 일자
    @Column(name="order_delete_flag")
    private String orderDeleteFlag; //의뢰 삭제 여부
}
