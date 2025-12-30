package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "class_teacher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(ClassTeacherId.class)
public class ClassTeacherEntity {
    @Id
    @Column(name = "class_no")
    private Long classNo;
    @Id
    @Column(name = "class_teacher_id") // member id랑 동일
    private Long classTeacherId;
    @Id
    @Column(name = "accept_no")
    private Long acceptNo;
}
