package com.aico.aibayo.repository.schedule;

import com.aico.aibayo.dto.schedule.ScheduleDto;
import com.aico.aibayo.dto.schedule.ScheduleSearchCondition;

import java.util.List;

public interface ScheduleRepositoryCustom {
    List<ScheduleDto> findAllByKinderNo(ScheduleSearchCondition condition);
    List<ScheduleDto> findAllByDay(ScheduleSearchCondition condition);
    List<ScheduleDto> findAllByClass(ScheduleSearchCondition condition);
    ScheduleDto findOneByScheduleNo(ScheduleSearchCondition condition);
    List<ScheduleDto> findAllByClassList(ScheduleSearchCondition condition);
    List<ScheduleDto> findAllByDayAndClassList(ScheduleSearchCondition condition);
}
