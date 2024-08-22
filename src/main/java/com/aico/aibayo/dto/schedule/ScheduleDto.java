package com.aico.aibayo.dto.schedule;

import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.entity.ScheduleClassEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private LocalDateTime boardDeleteDate;
    private LocalDateTime boardModifyDate;
    private LocalDateTime boardRegDate;
    private LocalDateTime scheduleStartDate;
    private LocalDateTime scheduleEndDate;
    private Double mapLat;
    private Double mapLng;
    private Long boardNo;
    private Long scheduleNo;
    private Long kinderNo;
    private Long classNo;
    private String boardTitle;
    private Long writer;
    private String boardContents;

    private List<ScheduleClassDto> classList;

    public ScheduleDto(Long boardNo, Long scheduleNo, Long kinderNo,
                       String boardTitle, Long writer, String boardContents,
                       LocalDateTime scheduleStartDate, LocalDateTime scheduleEndDate){
        this.boardNo = boardNo;
        this.scheduleNo = scheduleNo;
        this.kinderNo = kinderNo;
        this.boardTitle = boardTitle;
        this.writer = writer;
        this.boardContents = boardContents;
        this.scheduleStartDate = scheduleStartDate;
        this.scheduleEndDate = scheduleEndDate;
    }

    public ScheduleDto(Long boardNo, Long scheduleNo, Long kinderNo,
                       String boardTitle, Long writer, String boardContents,
                       LocalDateTime scheduleStartDate, LocalDateTime scheduleEndDate, List<ScheduleClassDto>classList,
                       Double mapLat, Double mapLng){
        this.boardNo = boardNo;
        this.scheduleNo = scheduleNo;
        this.kinderNo = kinderNo;
        this.boardTitle = boardTitle;
        this.writer = writer;
        this.boardContents = boardContents;
        this.scheduleStartDate = scheduleStartDate;
        this.scheduleEndDate = scheduleEndDate;
        this.classList = classList;
        this.mapLat = mapLat;
        this.mapLng = mapLng;
    }

    public ScheduleDto(Long boardNo, Long scheduleNo, Long kinderNo,
                       String boardTitle, Long writer, String boardContents,
                       LocalDateTime scheduleStartDate, LocalDateTime scheduleEndDate,
                       Double mapLat, Double mapLng){
        this.boardNo = boardNo;
        this.scheduleNo = scheduleNo;
        this.kinderNo = kinderNo;
        this.boardTitle = boardTitle;
        this.writer = writer;
        this.boardContents = boardContents;
        this.scheduleStartDate = scheduleStartDate;
        this.scheduleEndDate = scheduleEndDate;
        this.mapLat = mapLat;
        this.mapLng = mapLng;
    }

}
