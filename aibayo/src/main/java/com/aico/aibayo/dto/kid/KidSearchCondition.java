package com.aico.aibayo.dto.kid;

import lombok.Data;

@Data
public class KidSearchCondition {
    private Long kinderNo;

    private Long classNo;

    private Integer acceptStatus;

    private Integer inviteAcceptStatus;
}
