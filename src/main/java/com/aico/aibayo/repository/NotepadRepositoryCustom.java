package com.aico.aibayo.repository;

import com.aico.aibayo.dto.NotepadDto;
import com.aico.aibayo.dto.NotepadSearchCondition;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotepadRepositoryCustom {
    Page<NotepadDto> findAllByKinderNo(NotepadSearchCondition cond, Pageable pageable);
}