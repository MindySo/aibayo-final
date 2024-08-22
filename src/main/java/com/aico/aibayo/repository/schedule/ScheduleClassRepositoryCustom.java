package com.aico.aibayo.repository.schedule;

import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.schedule.ScheduleClassDto;
import com.aico.aibayo.entity.ScheduleClassEntity;

import java.util.List;

public interface ScheduleClassRepositoryCustom {
    List<ScheduleClassDto> findClassByScheduleNo(Long scheduleNo);
}
