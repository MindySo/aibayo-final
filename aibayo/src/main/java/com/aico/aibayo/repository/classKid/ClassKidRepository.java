package com.aico.aibayo.repository.classKid;

import com.aico.aibayo.entity.ClassKidEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassKidRepository extends JpaRepository<ClassKidEntity, Integer>, ClassKidRepositoryCustom {

//    List<ClassKidEntity> findByClassNoAndKidNo(Long classNo, Long kidNo);
}
