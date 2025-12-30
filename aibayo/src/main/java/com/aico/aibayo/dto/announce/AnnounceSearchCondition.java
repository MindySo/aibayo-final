package com.aico.aibayo.dto.announce;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnounceSearchCondition {
    private Long KinderNo;
    private Long boardNo;
    private String invisibleFlag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime boardRegDate;

    private Long announceNo;
    private Integer announceType;
    private String canComment;
    private String announcePrimary;

    private String keyword;
}
