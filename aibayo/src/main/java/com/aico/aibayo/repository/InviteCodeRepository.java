package com.aico.aibayo.repository;

import com.aico.aibayo.entity.InviteCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InviteCodeRepository extends JpaRepository<InviteCodeEntity, Long> {
    Optional<InviteCodeEntity> findByInviteIdAndInviteExpireFlag(Long inviteId, String bool);
}
