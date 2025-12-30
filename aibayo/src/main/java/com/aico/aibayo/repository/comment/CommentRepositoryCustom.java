package com.aico.aibayo.repository.comment;

import com.aico.aibayo.dto.comment.CommentDto;
import com.aico.aibayo.dto.comment.CommentSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<CommentDto>findAllByBoardNo(CommentSearchCondition condition, Pageable pageable);
}
