package com.aico.aibayo.service;

import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.schedule.ScheduleClassDto;
import com.aico.aibayo.entity.ScheduleClassEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ScheduleClassService {
    List<ScheduleClassDto> getClassByScheduleNo(Long scheduleNo);
}
