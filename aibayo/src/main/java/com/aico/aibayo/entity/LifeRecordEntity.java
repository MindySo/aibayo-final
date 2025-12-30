package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "life_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LifeRecordEntity {
    @Id
    @Column(name = "notepad_no")
    private Long notepadNo;
    @Column
    private int mood;
    @Column
    private int health;
    @Column
    private int temperature;
    @Column
    private int meal;
    @Column(name = "sleep_time")
    private int sleepTime;
    @Column(name = "defecation_status")
    private int defecationStatus;
}
