package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_no")
    private Long boardNo;
    @Column(name = "invisible_flag")
    private String invisibleFlag;
    @Column(name = "board_type")
    private Integer boardType;
    @Column
    private Long writer;
    @Column(name = "board_title")
    private String boardTitle;
    @Column(name = "board_contents", columnDefinition = "LONGTEXT")
    private String boardContents;
    @Column(name = "board_pic")
    private String boardPic;
    @Column(name = "board_reg_date")
    private LocalDateTime boardRegDate;
    @Column(name = "board_delete_date")
    private LocalDateTime boardDeleteDate;
    @Column(name = "board_modify_date")
    private LocalDateTime boardModifyDate;
    @Column(name = "kinder_no")
    private Long kinderNo;
}
