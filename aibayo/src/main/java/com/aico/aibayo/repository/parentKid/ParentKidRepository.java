package com.aico.aibayo.repository.parentKid;

import com.aico.aibayo.entity.ParentKidEntity;
import com.aico.aibayo.entity.ParentKidId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentKidRepository extends JpaRepository<ParentKidEntity, ParentKidId>, ParentKidRepositoryCustom {
    ParentKidEntity findById(Long id);
    Optional<ParentKidEntity> findByAcceptNo(Long acceptNo);
}
