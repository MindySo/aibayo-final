package com.aico.aibayo.service.schedule;

import com.aico.aibayo.dto.schedule.ScheduleDto;
import com.aico.aibayo.dto.schedule.ScheduleSearchCondition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ScheduleService {
    List<ScheduleDto> getAllByKinderNo(ScheduleSearchCondition condition);
    List<ScheduleDto> getAllByDay(ScheduleSearchCondition condition);
    List<ScheduleDto> getAllByClassNo(ScheduleSearchCondition condition);
    void insertSchedule(Map<String, Object> requestBody);
    ScheduleDto getOneByScheduleNo(ScheduleSearchCondition condition);
    void updateSchedule(Map<String, Object> requestBody);
    void deleteSchedule(Map<String, Object> requestBody);

    // user
    List<ScheduleDto> getAllByClassList(ScheduleSearchCondition condition);
    List<ScheduleDto> getAllByDayAndClassList(ScheduleSearchCondition condition);
}
