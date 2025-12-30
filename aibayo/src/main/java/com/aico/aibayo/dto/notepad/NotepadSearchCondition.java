package com.aico.aibayo.dto.notepad;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class NotepadSearchCondition {
    private Long kinderNo;
    private String invisibleFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime boardRegDate;

    private Long kidNo;
    private String dischargeFlag;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate notepadDate;

    private Integer acceptStatus;
}
