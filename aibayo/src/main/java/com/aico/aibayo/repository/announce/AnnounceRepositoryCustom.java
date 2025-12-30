package com.aico.aibayo.repository.announce;

import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.announce.AnnounceSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnounceRepositoryCustom {

    Page<AnnounceDto> findAllByKinderNoList(AnnounceSearchCondition condition, Pageable pageable);
    Page<AnnounceDto> findAllByKinderNoCard(AnnounceSearchCondition condition, Pageable pageable);
    AnnounceDto findByAnnounceNo(Long announceNo);
    Page<AnnounceDto> findKeywordByKinderNoList(AnnounceSearchCondition condition, Pageable pageable);
}
