package com.aico.aibayo.repository.announce;

import com.aico.aibayo.entity.AnnounceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnounceRepository extends JpaRepository<AnnounceEntity,Long>,AnnounceRepositoryCustom {
}
