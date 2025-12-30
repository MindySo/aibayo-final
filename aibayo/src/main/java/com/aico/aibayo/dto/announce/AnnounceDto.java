package com.aico.aibayo.dto.announce;

import com.aico.aibayo.entity.AnnounceEntity;
import com.aico.aibayo.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AnnounceDto {
    private Long boardNo;
    private Integer boardType;
    private Long writer;
    private String boardTitle;
    private String boardContents;
    private String invisibleFlag;
    private LocalDateTime boardRegDate;
    private Long kinderNo;
    private String boardPic;
    private LocalDateTime boardDeleteDate;
    private LocalDateTime boardModifyDate;

    private Long announceNo;
    private Integer announceType;
    private String canComment;
    private String announcePrimary;

    private Long id;

    private Integer roleNo;
    private String name;

    private Long commentNo;
    private Integer commentGroupNo;
    private LocalDateTime commentRegDate;
    private Long commentWriter;
    private String commentClass;
    private String commentContent;
    private String commentDeleteFlag;
    private Long commentCount;

    private boolean isAn;


    public AnnounceDto(Long boardNo, Integer boardType, Long writer, String boardTitle, String boardContents, String invisibleFlag, LocalDateTime boardRegDate, Long kinderNo, String boardPic, LocalDateTime boardDeleteDate, LocalDateTime boardModifyDate, Long announceNo, Integer announceType, String canComment, String announcePrimary, Long id, Integer roleNo, String name, Long commentNo, Integer commentGroupNo, LocalDateTime commentRegDate, Long commentWriter, String commentClass, String commentContent, String commentDeleteFlag, Long commentCount) {
        this.boardNo = boardNo;
        this.boardType = boardType;
        this.writer = writer;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.invisibleFlag = invisibleFlag;
        this.boardRegDate = boardRegDate;
        this.kinderNo = kinderNo;
        this.boardPic = boardPic;
        this.boardDeleteDate = boardDeleteDate;
        this.boardModifyDate = boardModifyDate;
        this.announceNo = announceNo;
        this.announceType = announceType;
        this.canComment = canComment;
        this.announcePrimary = announcePrimary;
        this.id = id;
        this.roleNo = roleNo;
        this.name = name;
        this.commentNo = commentNo;
        this.commentGroupNo = commentGroupNo;
        this.commentRegDate = commentRegDate;
        this.commentWriter = commentWriter;
        this.commentClass = commentClass;
        this.commentContent = commentContent;
        this.commentDeleteFlag = commentDeleteFlag;
        this.commentCount = commentCount;
    }

    public AnnounceDto(
            Integer announceType,Long announceNo,String announcePrimary,String canComment,Integer boardType,
            Long boardNo, Long writer, String boardContents, String boardTitle, String invisibleFlag,
            LocalDateTime boardRegDate, Long kinderNo, Integer roleNo, Long id, String name){
        this.announceType=announceType;
        this.announceNo=announceNo;
        this.announcePrimary=announcePrimary;
        this.canComment=canComment;
        this.boardType=boardType;
        this.boardNo=boardNo;
        this.writer=writer;
        this.boardContents=boardContents;
        this.boardTitle=boardTitle;
        this.invisibleFlag=invisibleFlag;
        this.boardRegDate=boardRegDate;
        this.kinderNo=kinderNo;
        this.roleNo=roleNo;
        this.id=id;
        this.name=name;
    }
    public AnnounceDto(
            Long announceNo, Long boardNo,Integer announceType,String canComment,String announcePrimary
    ){
        this.announceNo=announceNo;
        this.boardNo=boardNo;
        this.announceType=announceType;
        this.canComment=canComment;
        this.announcePrimary=announcePrimary;
    }

    public static AnnounceDto toDto(AnnounceEntity entity) {
        if (entity == null) { return null; }

        return new AnnounceDto(
                entity.getAnnounceNo(),
                entity.getBoardNo(),
                entity.getAnnounceType(),
                entity.getCanComment(),
                entity.getAnnouncePrimary()
        );
    }
    //board
    public AnnounceDto(
            Long boardNo, String invisibleFlag, Integer boardType, Long writer, String boardTitle, String boardContents,String boardPic,
            LocalDateTime boardRegDate, LocalDateTime boardDeleteDate, LocalDateTime boardModifyDate, Long kinderNo
    ){
        this.boardNo=boardNo;
        this.invisibleFlag=invisibleFlag;
        this.boardType=boardType;
        this.writer=writer;
        this.boardTitle=boardTitle;
        this.boardContents=boardContents;
        this.boardPic=boardPic;
        this.boardRegDate=boardRegDate;
        this.boardDeleteDate=boardDeleteDate;
        this.boardModifyDate=boardModifyDate;
        this.kinderNo=kinderNo;

    }

    public static AnnounceDto toDto(BoardEntity entity) {
        if (entity == null) { return null; }

        return new AnnounceDto(
                entity.getBoardNo(),
                entity.getInvisibleFlag(),
                entity.getBoardType(),
                entity.getWriter(),
                entity.getBoardTitle(),
                entity.getBoardContents(),
                entity.getBoardPic(),
                entity.getBoardRegDate(),
                entity.getBoardDeleteDate(),
                entity.getBoardModifyDate(),
                entity.getKinderNo()
        );
    }

}
