package com.aico.aibayo.dto.comment;

import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.entity.CommentEntity;
import com.aico.aibayo.entity.KidEntity;
import lombok.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long announceNo;

    private Integer roleNo;
    private String name;
    private Long kinderNo;

    private Integer announceType;
    private String canComment;
    private String announcePrimary;

    private Long boardNo;
    private Long commentNo;
    private Integer commentGroupNo;
    private LocalDateTime commentRegDate;
    private LocalDateTime commentModifyDate;
    private LocalDateTime commentDeleteDate;
    private Long commentWriter;
    private String commentClass;
    private String commentContent;
    private String invisibleFlag;

    private boolean isComment;
    private boolean isAdmin;

    public CommentDto(
            Integer roleNo, String name, Long kinderNo, Long boardNo,Long commentNo,
            Integer commentGroupNo,LocalDateTime commentRegDate, Long commentWriter,
            String commentClass,String commentContent, String invisibleFlag

    ){
        this.roleNo = roleNo;
        this.name = name;
        this.kinderNo = kinderNo;
        this.boardNo = boardNo;
        this.commentNo=commentNo;
        this.commentGroupNo = commentGroupNo;
        this.commentRegDate = commentRegDate;
        this.commentWriter = commentWriter;
        this.commentClass = commentClass;
        this.commentContent = commentContent;
        this.invisibleFlag=invisibleFlag;
    }

    public CommentDto(
            Long boardNo, String commentClass, String commentContent,
            Integer commentGroupNo, LocalDateTime commentRegDate, Long commentWriter){
            this.boardNo=boardNo;
            this.commentClass=commentClass;
            this.commentContent=commentContent;
            this.commentGroupNo=commentGroupNo;
            this.commentRegDate=commentRegDate;
            this.commentWriter=commentWriter;
    }

    public CommentDto(
            Long commentNo, Long boardNo, Long commentWriter, LocalDateTime commentRegDate,
            LocalDateTime commentModifyDate, LocalDateTime commentDeleteDate, String commentContent,
            String invisibleFlag, String commentClass, Integer commentGroupNo
    ) {
        this.commentNo=commentNo;
        this.boardNo=boardNo;
        this.commentWriter=commentWriter;
        this.commentRegDate=commentRegDate;
        this.commentModifyDate=commentModifyDate;
        this.commentDeleteDate=commentDeleteDate;
        this.commentContent=commentContent;
        this.invisibleFlag=invisibleFlag;
        this.commentClass=commentClass;
        this.commentGroupNo=commentGroupNo;
    }





    public static CommentDto toDto(CommentEntity entity) {
        if (entity == null) { return null; }

        return new CommentDto(
                entity.getCommentNo(),
                entity.getBoardNo(),
                entity.getCommentWriter(),
                entity.getCommentRegDate(),
                entity.getCommentModifyDate(),
                entity.getCommentDeleteDate(),
                entity.getCommentContent(),
                entity.getInvisibleFlag(),
                entity.getCommentClass(),
                entity.getCommentGroupNo()
        );
    }


}
