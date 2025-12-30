package com.aico.aibayo.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class TeacherKinderId {
    private Long teacherId;
    private Long kinderNo;
    private Long acceptNo;
}
