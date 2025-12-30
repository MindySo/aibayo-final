package com.aico.aibayo.dto.classTeacher;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ClassTeacherDto {
    private Long classNo;
    private Long classTeacherId; // member id
    private Long acceptNo;

    private String name;
    private Integer acceptStatus;
    private String className;
}
