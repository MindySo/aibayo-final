package com.aico.aibayo.service;

import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.schedule.ScheduleClassDto;
import com.aico.aibayo.repository.schedule.ScheduleClassRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleClassServiceImpl implements ScheduleClassService {
    private final ScheduleClassRepository scheduleClassRepository;
    @Override
    public List<ScheduleClassDto> getClassByScheduleNo(Long scheduleNo) {
        return scheduleClassRepository.findClassByScheduleNo(scheduleNo);
    }
}
