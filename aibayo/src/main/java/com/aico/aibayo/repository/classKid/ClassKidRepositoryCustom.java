package com.aico.aibayo.repository.classKid;

import com.aico.aibayo.dto.classKid.ClassKidDto;

import java.util.List;

public interface ClassKidRepositoryCustom {

    List<ClassKidDto> findAllByClassNoAndKidNo(Long classNo, Long kidNo);

    List<ClassKidDto> findAllByClassNo(Long classNo);
}
