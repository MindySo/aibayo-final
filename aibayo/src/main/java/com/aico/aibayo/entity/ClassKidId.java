package com.aico.aibayo.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class ClassKidId implements Serializable {
    private Long classNo;
    private Long kidNo;
    private Long acceptNo;
}