package com.aico.aibayo.service.comment;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.comment.CommentDto;
import com.aico.aibayo.dto.comment.CommentSearchCondition;
import com.aico.aibayo.entity.AnnounceEntity;
import com.aico.aibayo.entity.BoardEntity;
import com.aico.aibayo.entity.CommentEntity;
import com.aico.aibayo.repository.BoardRepository;
import com.aico.aibayo.repository.announce.AnnounceRepository;
import com.aico.aibayo.repository.comment.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final AnnounceRepository announceRepository;


    private static final int PAGE_SIZE=10;


    @Override
    public Page<CommentDto> findAllByBoardNo(CommentSearchCondition condition, HashMap<String, Object> hashMap) {
        int page = (int) hashMap.get("page");
        String type = (String) hashMap.get("type");
        int pagesize=10;

        Pageable pageable = PageRequest.of(page - 1, pagesize);
        return commentRepository.findAllByBoardNo(condition,pageable);
    }

    @Override
    public long countByBoardNoAndInvisibleFlag(Long boardNo,String invisibleFlag) {
        return commentRepository.countByBoardNoAndInvisibleFlag(boardNo,"0");
    }

    @Override
    @Transactional
    public CommentDto insertComment(CommentDto commentDto) {
        // Check if boardNo is null
        if (commentDto.getBoardNo() == null) {
            throw new IllegalArgumentException("BoardNo must not be null");
        }

       log.info("commentDto : "+ commentDto);
        CommentEntity commentEntity = CommentEntity.builder()
                .commentWriter(commentDto.getCommentWriter())
                .commentNo(commentDto.getCommentNo())
                .boardNo(commentDto.getBoardNo())
                .commentWriter(commentDto.getCommentWriter())
                .commentRegDate(LocalDateTime.now())
                .commentContent(commentDto.getCommentContent())
                .commentClass(commentDto.getCommentClass())
                .commentGroupNo(commentDto.getCommentGroupNo())
                .invisibleFlag(commentDto.getInvisibleFlag())
                .build();

        CommentEntity insertComment = commentRepository.save(commentEntity);

        return CommentDto.toDto(insertComment);
    }

    @Override
    public CommentDto deleteComment(CommentDto commentDto) {
        CommentEntity commentEntity = commentRepository.findById(commentDto.getCommentNo()).orElse(null);
        assert commentEntity != null;
        commentEntity.setInvisibleFlag(BooleanEnum.TRUE.getBool());
        commentEntity.setCommentDeleteDate(LocalDateTime.now());
        log.info("delete comment : "+commentEntity);

        CommentEntity save = commentRepository.save(commentEntity);
        return CommentDto.toDto(save);
    }

    @Override
    @Transactional
    public CommentDto updateComment(CommentDto commentDto) {
        CommentEntity commentEntity =
        commentRepository.findById(commentDto.getCommentNo()).orElse(null);
        assert commentEntity != null;
        commentEntity.setCommentContent(commentDto.getCommentContent());
        commentEntity.setCommentModifyDate(LocalDateTime.now());

        CommentEntity save = commentRepository.save(commentEntity);
        CommentDto dto = CommentDto.toDto(save);
        dto.setComment(true);
        return dto;

    }


}
