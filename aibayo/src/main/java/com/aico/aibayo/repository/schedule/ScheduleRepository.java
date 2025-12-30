package com.aico.aibayo.repository.schedule;

import com.aico.aibayo.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long>, ScheduleRepositoryCustom {

}
