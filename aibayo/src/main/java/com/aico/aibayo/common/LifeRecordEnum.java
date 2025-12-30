package com.aico.aibayo.common;

import lombok.Getter;

@Getter
public enum LifeRecordEnum {
    MOOD_GOOD(0), MOOD_NORMAL(1),MOOD_BAD(2),
    HEALTH_GOOD(0), HEALTH_NORMAL(1), HEALTH_BAD(2),
    TEMPERATURE_GOOD(0), TEMPERATURE_NORMAL(1), TEMPERATURE(2),
    MEAL_NORMAL(0), MEAL_MANY(1), MEAL_LITTLE(2), MEAL_NOT(3),
    SLEEP_TIME_NOT(0), SLEEP_TIME_IN_HOUR(1), SLEEP_TIME_UNDER_HOUR_HALF(2),
        SLEEP_TIME_UNDER_TWO_HOUR(3), SLEEP_TIME_OVER_TWO_HOUR(4),
    DEFECATION_STATUS_NORMAL(0), DEFECATION_STATUS_HARD(1), DEFECATION_STATUS_SOFT(2),
        DEFECATION_STATUS_TROT(3), DEFECATION_STATUS_NOT(4);


    private int num;

    LifeRecordEnum(int num) {
        this.num = num;
    }

    static LifeRecordEnum findByNumber(int num) {
        for (LifeRecordEnum lifeRecord : LifeRecordEnum.values()) {
            if (num == lifeRecord.num) {
                return lifeRecord;
            }
        }

        throw new IllegalArgumentException("일치하는 값이 존재하지 않습니다.");
    }
}
