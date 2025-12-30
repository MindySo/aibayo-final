package com.aico.aibayo.repository.classManage;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.schedule.ScheduleSearchCondition;
import com.querydsl.core.types.Projections;

import java.util.List;

public interface ClassRepositoryCustom {
    List<ClassDto> findAllByMemberId(Long id);
    List<ClassDto> findAllByKidNo(Long kidNo);

    List<ClassDto> findAllByKinderNo(Long kinderNo);
    List<ClassDto> findAddableClassByKinderNo(Long kinderNo);
    List<ClassDto> findClassByKinderNoAndTeacherId(Long kinderNo, Long id);

}
