package com.aico.aibayo.dto.teacher;

import com.aico.aibayo.common.AcceptStatusEnum;
import lombok.Data;

@Data
public class TeacherSearchCondition {
    private Long kinderNo;
    private Integer acceptStatus;
    private Long classNo;
}
