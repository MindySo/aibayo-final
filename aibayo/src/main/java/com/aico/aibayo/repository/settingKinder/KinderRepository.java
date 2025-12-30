package com.aico.aibayo.repository.settingKinder;

import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.entity.RegisterKinderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KinderRepository extends JpaRepository<RegisterKinderEntity,Long> {

    List<RegisterKinderEntity> findAllByDeleteFlag(String deleteFlag);
}
