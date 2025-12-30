package com.aico.aibayo.repository.comment;

import com.aico.aibayo.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long>,CommentRepositoryCustom {
    long countByBoardNoAndInvisibleFlag(Long boardNo , String invisibleFlag);
}
