package com.aico.aibayo.repository;

import com.aico.aibayo.entity.AcceptLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcceptLogRepository extends JpaRepository<AcceptLogEntity, Long> {
    Optional<AcceptLogEntity> findByAcceptNo(Long acceptNo);
}
