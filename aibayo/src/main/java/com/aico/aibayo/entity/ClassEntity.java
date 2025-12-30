package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "class")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ClassEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_no")
    private Long classNo;
    @Column(name = "class_name")
    private String className;
    @Column(name = "class_age")
    private String classAge;
    @Column(name = "kinder_no")
    private Long kinderNo;
    @Column(name = "class_reg_date")
    private LocalDateTime classRegDate;
    @Column(name = "class_modify_date")
    private LocalDateTime classModifyDate;
    @Column(name = "class_delete_date")
    private LocalDateTime classDeleteDate;
    @Column(name = "class_delete_flag")
    private String classDeleteFlag;
}
