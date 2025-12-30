package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "class_kid")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(ClassKidId.class)
public class ClassKidEntity {
    @Id
    @Column(name = "class_no")
    private Long classNo;
    @Id
    @Column(name = "kid_no")
    private Long kidNo;
    @Id
    @Column(name = "accept_no")
    private Long acceptNo;

}
