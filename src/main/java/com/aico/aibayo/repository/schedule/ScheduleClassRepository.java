package com.aico.aibayo.repository.schedule;

import com.aico.aibayo.entity.ScheduleClassEntity;
import com.aico.aibayo.entity.ScheduleClassId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleClassRepository extends JpaRepository<ScheduleClassEntity, ScheduleClassId>, ScheduleClassRepositoryCustom {
}
