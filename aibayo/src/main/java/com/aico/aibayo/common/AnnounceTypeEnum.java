package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum AnnounceTypeEnum {
    TEACHER(1),
    NOTICE(2),
    SCHEDULE(3),
    EDUCATION(4),
    PAYMENT(5);

    private int num;

    AnnounceTypeEnum(int num) {
        this.num = num;
    }

    public static AnnounceTypeEnum findByNumber(int num){
        for(AnnounceTypeEnum announceType : AnnounceTypeEnum.values()){{
            if(num == announceType.num){
                return announceType;
            }
        }}
        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
