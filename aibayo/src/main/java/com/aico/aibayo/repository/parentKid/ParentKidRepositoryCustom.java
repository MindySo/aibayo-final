package com.aico.aibayo.repository.parentKid;

import com.aico.aibayo.dto.ParentKidDto;

public interface ParentKidRepositoryCustom {
    ParentKidDto findByKidMainParent(Long kidNo);
}
