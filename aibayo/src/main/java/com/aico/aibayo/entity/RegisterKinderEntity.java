package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "register_kinder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RegisterKinderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kinder_no")
    private Long kinderNo; // 유치원 번호

    @Column(name = "kinder_open_time")
    private LocalTime kinderOpenTime; // 유치원 등원시간

    @Column(name = "kinder_close_time")
    private LocalTime kinderCloseTime; // 유치원 하원시간

    @Column(name = "kinder_name")
    private String kinderName; // 유치원 이름

    @Column(name = "kinder_post_code")
    private String kinderPostCode; //유치원 우편번호

    @Column(name = "kinder_loc_no")
    private String kinderLocNo; //유치원 지역 번호

    @Column(name = "kinder_mid_no")
    private String kinderMidNo;

    @Column(name = "kinder_end_no")
    private String kinderEndNo;

    @Column(name = "kinder_addr")
    private String kinderAddr; //유치원 주소

    @Column(name = "kinder_addr_detail")
    private String kinderAddrDetail; //유치원 주소

    @Column(name = "kinder_reg_date")
    private LocalDateTime kinderRegDate; // 유치원 등록일자

    @Column(name = "kinder_modify_date")
    private LocalDateTime kinderModifyDate; // 유치원 수정일자

    @Column(name = "kinder_delete_date")
    private LocalDateTime kinderDeleteDate; // 유치원 삭제일자

    @Column(name = "sido_list")
    private String sidoList; // API 시도 리스트

    @Column(name = "sgg_list")
    private String sggList; // API 시군구 리스트

//    @Column(name = "map_lat")
//    private Double mapLat;
//
//    @Column(name = "map_lng")
//    private Double mapLng;
//
    @Column(name = "delete_flag")
    private String deleteFlag;

}
