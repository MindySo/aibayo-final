package com.aico.aibayo.repository.announce;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.announce.AnnounceSearchCondition;
import com.aico.aibayo.entity.*;
import com.aico.aibayo.service.announce.AnnounceService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.querydsl.core.types.dsl.Wildcard.count;

@RequiredArgsConstructor
public class AnnounceRepositoryCustomImpl implements AnnounceRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final QBoardEntity board = QBoardEntity.boardEntity;
    private final QAnnounceEntity announce =QAnnounceEntity.announceEntity;
    private final QMemberEntity member =QMemberEntity.memberEntity;
    private final QAcceptLogEntity acceptLog=QAcceptLogEntity.acceptLogEntity;
    private final QCommentEntity comment = QCommentEntity.commentEntity;

    @Override
    public AnnounceDto findByAnnounceNo(Long announceNo) {
        return jpaQueryFactory
                .select(Projections.constructor(AnnounceDto.class,
                        announce.announceType,
                        announce.announceNo,
                        announce.announcePrimary,
                        announce.canComment,
                        board.boardType,
                        board.boardNo,
                        board.writer,
                        board.boardContents,
                        board.boardTitle,
                        board.invisibleFlag,
                        board.boardRegDate,
                        board.kinderNo,
                        member.roleNo,
                        member.id,
                        member.name
                ))
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .leftJoin(comment).on(comment.boardNo.eq(board.boardNo)
                        .and(announce.canComment.eq("1")))
                .orderBy(comment.commentNo.desc())
                .where(announce.announceNo.eq(announceNo))
                .limit(1)  // 최신 댓글만 가져오기 위해 limit 사용
                .fetchOne();
    }

    @Override
    public Page<AnnounceDto> findKeywordByKinderNoList(AnnounceSearchCondition condition, Pageable pageable) {
        // 검색어를 얻어오고, `LIKE` 패턴을 추가합니다.
        String keyword = condition.getKeyword() != null ? "%" + condition.getKeyword() + "%" : null;

        // 메인 쿼리
        List<AnnounceDto> announces = jpaQueryFactory
                .select(Projections.constructor(AnnounceDto.class,
                        announce.announceType,
                        announce.announceNo,
                        announce.announcePrimary,
                        announce.canComment,
                        board.boardType,
                        board.boardNo,
                        board.writer,
                        board.boardContents,
                        board.boardTitle,
                        board.invisibleFlag,
                        board.boardRegDate,
                        board.kinderNo,
                        member.roleNo,
                        member.id,
                        member.name))
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .where(
                        getInvisibleFlagEq(board),
                        getKinderNoEq(condition.getKinderNo(), member),
                        getBoardRegDateEq(condition.getBoardRegDate(), board),
                        getAnnouncePrimaryEq(condition.getAnnouncePrimary(), announce),
                        keyword != null ? board.boardTitle.like(keyword) : null
                )
                .orderBy(board.boardRegDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(board.count())
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .where(
                        getInvisibleFlagEq(board),
                        getKinderNoEq(condition.getKinderNo(), member),
                        getBoardRegDateEq(condition.getBoardRegDate(), board),
                        getAnnouncePrimaryEq(condition.getAnnouncePrimary(), announce),
                        keyword != null ? board.boardTitle.like(keyword) : null
                );

        // 페이지 결과 반환
        return PageableExecutionUtils.getPage(announces, pageable, countQuery::fetchOne);
    }


    @Override
    public Page<AnnounceDto> findAllByKinderNoList(AnnounceSearchCondition condition, Pageable pageable) {
        List<AnnounceDto>announces=jpaQueryFactory
                .select(Projections.constructor(AnnounceDto.class,
                        announce.announceType,
                        announce.announceNo,
                        announce.announcePrimary,
                        announce.canComment,
                        board.boardType,
                        board.boardNo,
                        board.writer,
                        board.boardContents,
                        board.boardTitle,
                        board.invisibleFlag,
                        board.boardRegDate,
                        board.kinderNo,
                        member.roleNo,
                        member.id,
                        member.name ))
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .where(
                        getInvisibleFlagEq(board),
                        getKinderNoEq(condition.getKinderNo(), member),
                        getBoardRegDateEq(condition.getBoardRegDate(), board),
                        getAnnouncePrimaryEq(condition.getAnnouncePrimary(), announce),
                        getAnnounceTypeGt(condition.getAnnounceType())
                )
                .orderBy(
//                        announce.announcePrimary.desc(),
                        board.boardRegDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(announce.count())
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .where(
                        getInvisibleFlagEq(board),
                        getKinderNoEq(condition.getKinderNo(), member),
                        getBoardRegDateEq(condition.getBoardRegDate(), board),
                        getAnnouncePrimaryEq(condition.getAnnouncePrimary(), announce),
                        getAnnounceTypeGt(condition.getAnnounceType())
                );

        return PageableExecutionUtils.getPage(announces, pageable, count::fetchOne);
    }

    @Override
    public Page<AnnounceDto> findAllByKinderNoCard(AnnounceSearchCondition condition, Pageable pageable) {
        List<AnnounceDto>announces=jpaQueryFactory
                .select(Projections.constructor(AnnounceDto.class,
                        announce.announceType,
                        announce.announceNo,
                        announce.announcePrimary,
                        announce.canComment,
                        board.boardType,
                        board.boardNo,
                        board.writer,
                        board.boardContents,
                        board.boardTitle,
                        board.invisibleFlag,
                        board.boardRegDate,
                        board.kinderNo,
                        member.roleNo,
                        member.id,
                        member.name
                        ))
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .where(
                        getInvisibleFlagEq(board),
                        getKinderNoEq(condition.getKinderNo(), member),
                        getBoardRegDateEq(condition.getBoardRegDate(), board),
                        getAnnounceTypeGt(condition.getAnnounceType())

                )
                .orderBy(board.boardRegDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> count = jpaQueryFactory.select(announce.count())
                .from(announce)
                .join(board).on(board.boardNo.eq(announce.boardNo))
                .join(member).on(board.writer.eq(member.id))
                .where(
                        getInvisibleFlagEq(board),
                        getKinderNoEq(condition.getKinderNo(), member),
                        getBoardRegDateEq(condition.getBoardRegDate(), board),
                        getAnnounceTypeGt(condition.getAnnounceType())
                );

        return PageableExecutionUtils.getPage(announces, pageable, count::fetchOne);
    }

    private BooleanExpression getAnnounceTypeGt(Integer announceType) {
        return announceType == null ? null :
                announce.announceType.gt(announceType);
    }

    private BooleanExpression getAnnouncePrimaryEq(String announcePrimary, QAnnounceEntity announce) {
        return announcePrimary == null ? null :
                announce.announcePrimary.eq(announcePrimary);
    }


    private BooleanExpression isValidAcceptStatus() {
        return acceptLog.acceptStatus.eq(AcceptStatusEnum.ACCEPT.getStatus());
    }
    private BooleanExpression getBoardRegDateEq(LocalDateTime boardRegDate, QBoardEntity board) {
        if (boardRegDate == null) {
            return null;
        }

        LocalDate date = boardRegDate.toLocalDate();
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX).minusSeconds(1L);

        return board.boardRegDate.between(startDateTime, endDateTime);
    }
    private BooleanExpression getKinderNoEq(Long kinderNo, QMemberEntity member) {
        return kinderNo == null ? null : member.kinderNo.eq(kinderNo);
    }
    private BooleanExpression getInvisibleFlagEq(QBoardEntity board) {
        return board.invisibleFlag.eq(BooleanEnum.FALSE.getBool());
    }
}
