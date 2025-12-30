package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum SidoInfoEnum {
    ALL("00", "00", "전국"),
    SEOUL("11", "11","서울"),
    BUSAN("21", "26", "부산"),
    DAEGU("22", "27", "대구"),
    INCHEON("23", "28", "인천"),
    GWANGJU("24", "29", "광주"),
    DAEJEON("25", "30", "대전"),
    ULSAN("26", "31", "울산"),
    SEJONG("29", "36", "세종"),
    GYEONGGI("31", "41", "경기"),
    GANGWON("32", "51", "강원"),
    CHUNGBUK("33", "43", "충북"),
    CHUNGNAM("34", "44", "충남"),
    JEOLLABUK("35", "52", "전북"),
    JEOLLANAM("36", "46", "전남"),
    GYEONGBUK("37", "47", "경북"),
    GYEONGNAM("38", "48", "경남"),
    JEJU("39", "50", "제주");

    private final String sidoCode;
    private final String kinderSidoCode;
    private final String sidoName;

    SidoInfoEnum(String sidoCode, String kinderSidoCode, String sidoName) {
        this.sidoCode = sidoCode;
        this.kinderSidoCode = kinderSidoCode;
        this.sidoName = sidoName;
    }

    public static SidoInfoEnum findBySidoCode(String sidoCode) {
        for (SidoInfoEnum sidoInfo : SidoInfoEnum.values()) {
            if (sidoCode.equals(sidoInfo.sidoCode)) {
                return sidoInfo;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }

    public static SidoInfoEnum findByKinderSidoCode(String kinderSidoCode) {
        for (SidoInfoEnum sidoInfo : SidoInfoEnum.values()) {
            if (kinderSidoCode.equals(sidoInfo.kinderSidoCode)) {
                return sidoInfo;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }

    public static SidoInfoEnum findBySidoName(String sidoName) {
        for (SidoInfoEnum sidoInfo : SidoInfoEnum.values()) {
            if (sidoName.equals(sidoInfo.sidoName)) {
                return sidoInfo;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
