package com.aico.aibayo.repository.notepad;

import com.aico.aibayo.dto.notepad.NotepadDto;
import com.aico.aibayo.dto.notepad.NotepadSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotepadRepositoryCustom {
    Page<NotepadDto> findAllByKinderNo(NotepadSearchCondition condition, Pageable pageable);
    Page<NotepadDto> findAllByKidNo(NotepadSearchCondition condition, Pageable pageable);
    NotepadDto findByNotepadNo(Long notepadNo);
}
