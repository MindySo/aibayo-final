package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_kinder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(TeacherKinderId.class)
public class TeacherKinderEntity {
    @Id
    @Column(name = "teacher_id")
    private Long teacherId;
    @Id
    @Column(name = "kinder_no")
    private Long kinderNo;
    @Id
    @Column(name = "accept_no")
    private Long acceptNo;
}
