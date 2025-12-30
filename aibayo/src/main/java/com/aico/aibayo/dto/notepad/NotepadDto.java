package com.aico.aibayo.dto.notepad;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NotepadDto {
    private Long boardNo;
    private Integer boardType;
    private Long writer;
    private String boardTitle;
    private String boardContents;
    private String invisibleFlag;
    private LocalDateTime boardRegDate;
    private Long boardKinderNo;

    private Long id;
    private String name;
    private Long kinderNo;

    private Long notepadNo;
    private LocalDate notepadDate;
    private String hasLifeRecord;

    private Integer mood;
    private Integer health;
    private Integer temperature;
    private Integer meal;
    private Integer sleepTime;
    private Integer defecationStatus;

    private Long classNo;
    private Long kidNo;

    private String className;

    private String kidName;

    public NotepadDto(Long boardNo, Integer boardType, Long writer, String boardTitle,
                      String boardContents, String invisibleFlag, LocalDateTime boardRegDate, Long boardKinderNo,
                      Long id, String name, Long kinderNo, Long notepadNo, LocalDate notepadDate) {
        this.boardNo = boardNo;
        this.boardType = boardType;
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.invisibleFlag = invisibleFlag;
        this.boardRegDate = boardRegDate;
        this.boardKinderNo = boardKinderNo;
        this.id = id;
        this.name = name;
        this.kinderNo = kinderNo;
        this.notepadNo = notepadNo;
        this.notepadDate = notepadDate;
    }

}
