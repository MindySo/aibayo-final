package com.aico.aibayo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ClassTeacherId {
    private Long classNo;
    private Long classTeacherId;
    private Long acceptNo;
}
