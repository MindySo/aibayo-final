package com.aico.aibayo.service.announce;


import com.aico.aibayo.common.AnnounceTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.announce.AnnounceSearchCondition;
import com.aico.aibayo.entity.AnnounceEntity;
import com.aico.aibayo.entity.BoardEntity;
import com.aico.aibayo.repository.BoardRepository;
import com.aico.aibayo.repository.announce.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class AnnounceServiceImpl implements AnnounceService {
    private final AnnounceRepository announceRepository;
    private final BoardRepository boardRepository;
    private static final int PAGE_SIZE_CARD = 6;
    private static final int PAGE_SIZE_LIST = 15;
    private static final int PAGE_SIZE_PRIMARY = 5;
    private static final int PAGE_SIZE_MAIN = 2;

    @Override
    public Page<AnnounceDto> findAllByKinderNoList(AnnounceSearchCondition condition, HashMap<String, Object>hashMap) {
        int page = (int) hashMap.get("page");
        String type = (String) hashMap.get("type");
        int pagesize=0;
        if(type.equals("card")){
            pagesize=PAGE_SIZE_CARD;
        }else if(type.equals("list")){
            pagesize=PAGE_SIZE_LIST;
        }else if(type.equals("listPrimary")) {
            pagesize = PAGE_SIZE_PRIMARY;
        }


        Pageable pageable = PageRequest.of(page - 1, pagesize);
        return announceRepository.findAllByKinderNoList(condition,pageable);
    }

    @Override
    public Page<AnnounceDto> findAllByKinderNoCard(AnnounceSearchCondition condition, HashMap<String, Object> hashMap) {
        int page = (int) hashMap.get("page");
        String type = (String) hashMap.get("type");
        int pagesize=0;
        if(type.equals("card")){
            pagesize=PAGE_SIZE_CARD;
        }else if(type.equals("list")){
            pagesize=PAGE_SIZE_LIST;
        }
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        return announceRepository.findAllByKinderNoCard(condition,pageable);
    }
    @Override
    public AnnounceDto findByAnnounceNo(Long announceNo) {
        return announceRepository.findByAnnounceNo(announceNo);
    }

    @Override
    @Transactional
    public void insertAnnounce(AnnounceDto announceDto) {
        BoardEntity boardEntity = BoardEntity.builder()
                .invisibleFlag(BooleanEnum.FALSE.getBool())
                .boardType(announceDto.getBoardType())
                .writer(announceDto.getWriter())
                .boardTitle(announceDto.getBoardTitle())
                .boardContents(announceDto.getBoardContents())
                .boardRegDate(LocalDateTime.now())
                .kinderNo(announceDto.getKinderNo())
                .build();
        BoardEntity savedBoard = boardRepository.save(boardEntity);
        AnnounceEntity announceEntity = AnnounceEntity.builder()
                .boardNo(savedBoard.getBoardNo())
                .announceType(announceDto.getAnnounceType())
                .announcePrimary(announceDto.getAnnouncePrimary())
                .canComment(announceDto.getCanComment())
                .build();
        AnnounceEntity savedAnnounce = announceRepository.save(announceEntity);
    }

    @Override
    @Transactional
    public void updateAnnounce(AnnounceDto announceDto) {
        BoardEntity boardEntity =
            boardRepository.findById(announceDto.getBoardNo()).orElse(null);
            assert boardEntity != null;
            boardEntity.setBoardTitle(announceDto.getBoardTitle());
            boardEntity.setBoardContents(announceDto.getBoardContents());
            boardEntity.setBoardModifyDate(LocalDateTime.now());

            boardRepository.save(boardEntity);

        AnnounceEntity announceEntity =
            announceRepository.findById(announceDto.getAnnounceNo()).orElse(null);
            assert announceEntity != null;
            announceEntity.setAnnounceType(announceDto.getAnnounceType());
            announceEntity.setAnnouncePrimary(announceDto.getAnnouncePrimary());
            announceEntity.setCanComment(announceDto.getCanComment());

        announceRepository.save(announceEntity);
    }

    @Override
    public AnnounceDto deleteAnnounce(AnnounceDto announceNo) {
        BoardEntity boardEntity =
        boardRepository.findById(announceNo.getBoardNo()).orElse(null);
        assert boardEntity != null;
        boardEntity.setBoardDeleteDate(LocalDateTime.now());
        boardEntity.setInvisibleFlag(BooleanEnum.TRUE.getBool());

        BoardEntity save = boardRepository.save(boardEntity);
        return AnnounceDto.toDto(save);
    }

    @Override
    public Page<AnnounceDto> findKeywordByKinderNoList(AnnounceSearchCondition condition, HashMap<String, Object> hashMap) {
        int page = (int) hashMap.get("page");
        String type = (String) hashMap.get("type");
        int pagesize=0;
        if(type.equals("card")){
            pagesize=PAGE_SIZE_CARD;
        }else if(type.equals("list")){
            pagesize=PAGE_SIZE_LIST;
        }else if(type.equals("listPrimary")) {
            pagesize = PAGE_SIZE_PRIMARY;
        }


        Pageable pageable = PageRequest.of(page - 1, pagesize);
        return announceRepository.findKeywordByKinderNoList(condition,pageable);
    }

    @Override
    public List<AnnounceDto> findAllByKinderNoMain(Long kinderNo) {
        AnnounceSearchCondition condition = new AnnounceSearchCondition();
        condition.setKinderNo(kinderNo);
        condition.setAnnounceType(AnnounceTypeEnum.TEACHER.getNum());

        Pageable pageable = PageRequest.of(0, PAGE_SIZE_MAIN);
        Page<AnnounceDto> announceDtoPage = announceRepository.findAllByKinderNoCard(condition, pageable);

        return announceDtoPage.getContent();
    }


}
