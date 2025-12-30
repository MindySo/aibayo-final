package com.aico.aibayo.repository;

import com.aico.aibayo.entity.UploadFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepository extends JpaRepository<UploadFileEntity, Long> {
}
