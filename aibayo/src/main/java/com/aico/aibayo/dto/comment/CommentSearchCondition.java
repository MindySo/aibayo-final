package com.aico.aibayo.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentSearchCondition {
    private Long boardNo;
    private String invisibleFlag;

    private Integer roleNo;
    private String name;
    private Long kinderNo;

    private Integer commentGroupNo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentRegDate;
    private Long commentWriter;
    private String commentClass;
    private String commentContent;
    private Long commentNo;


}
