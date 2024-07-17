package com.aico.aibayo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardEntity is a Querydsl query type for BoardEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardEntity extends EntityPathBase<BoardEntity> {

    private static final long serialVersionUID = 1124863426L;

    public static final QBoardEntity boardEntity = new QBoardEntity("boardEntity");

    public final StringPath boardContents = createString("boardContents");

    public final DateTimePath<java.time.LocalDateTime> boardDeleteDate = createDateTime("boardDeleteDate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> boardModifyDate = createDateTime("boardModifyDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> boardNo = createNumber("boardNo", Integer.class);

    public final StringPath boardPic = createString("boardPic");

    public final StringPath boardRegDate = createString("boardRegDate");

    public final StringPath boardTitle = createString("boardTitle");

    public final NumberPath<Integer> boardType = createNumber("boardType", Integer.class);

    public final StringPath invisibleFlag = createString("invisibleFlag");

    public final StringPath writer = createString("writer");

    public QBoardEntity(String variable) {
        super(BoardEntity.class, forVariable(variable));
    }

    public QBoardEntity(Path<? extends BoardEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardEntity(PathMetadata metadata) {
        super(BoardEntity.class, metadata);
    }

}
