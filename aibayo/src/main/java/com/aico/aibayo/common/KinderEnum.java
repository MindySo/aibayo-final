package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum KinderEnum {
    ANNOUNCE_ON(0),ANNOUNCE_OFF(1),
    NOTEPAD_ON(0),NOTEPAD_OFF(1),
    MEAL_ON(0),MEAL_OFF(1),
    MEDICATION_ON(0),MEDICATION_OFF(1),
    HOME_ON(0),HOME_OFF(1),
    ATTENDANCE_ON(0),ATTENDANCE_OFF(1),
    SCHEDULE_ON(0), SCHEDULE_OFF(1),
    PICK_DROP_ON(0),PICK_DROP_OFF(1),
    LIFE_RECORD_ON(0),LIFE_RECORD_OFF(1),
    CHAT_ON(0),CHAT_OFF(0);

    private int num;

    KinderEnum(int num) {
        this.num = num;
    }
    static KinderEnum findByNumber ( int num ) {
        for (KinderEnum function : KinderEnum.values()) {
            {
                if (num == function.num) {
                    return function;
                }
            }
        }
        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
