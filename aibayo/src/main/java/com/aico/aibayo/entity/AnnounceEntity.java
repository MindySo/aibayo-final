package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "announce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ToString

public class AnnounceEntity {
    @Id
    @Column(name="announce_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long announceNo; //공지 번호
    @Column(name="board_no")
    private Long boardNo; //게시글 번호
    @Column(name="announce_type")
    private Integer announceType; //공지 분류
    @Column(name="can_comment")
    private String canComment; //댓글 허용 여부
    @Column(name="announce_primary")
    private String announcePrimary; //중요 공지 여부
}
