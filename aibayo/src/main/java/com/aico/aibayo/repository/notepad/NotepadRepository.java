package com.aico.aibayo.repository.notepad;

import com.aico.aibayo.entity.NotepadEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotepadRepository extends JpaRepository<NotepadEntity, Long>, NotepadRepositoryCustom {
}
