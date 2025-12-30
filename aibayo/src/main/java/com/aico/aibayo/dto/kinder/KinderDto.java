package com.aico.aibayo.dto.kinder;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KinderDto {
    private Long kinderNo; // 유치원 번호
    private LocalTime kinderOpenTime; // 유치원 등원시간
    private LocalTime kinderCloseTime; // 유치원 하원시간
    private String kinderName; // 유치원 이름
    private String kinderPostCode; //유치원 우편번호
    private String kinderLocNo; //유치원 지역 번호
    private String kinderMidNo;
    private String kinderEndNo;
    private String kinderAddr; //유치원 주소
    private String kinderAddrDetail; //유치원 주소
    private LocalDateTime kinderRegDate; // 유치원 등록일자
    private LocalDateTime kinderModifyDate; // 유치원 수정일자
    private LocalDateTime kinderDeleteDate; // 유치원 삭제일자
    private String sidoList; // API 시도 리스트
    private String sggList; // API 시군구 리스트

}
