package com.aico.aibayo.repository.classManage;

import com.aico.aibayo.entity.ClassEntity;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<ClassEntity, Integer>, ClassRepositoryCustom {
    List<ClassEntity> findAllByKinderNoAndClassDeleteFlagEquals(Long kinderNo, String classDeleteFlag);

    ClassEntity findByClassNo(Long classNo);
}
