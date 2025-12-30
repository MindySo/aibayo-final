package com.aico.aibayo.repository.member;

import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {
    Boolean existsByUsername(String username);
    Optional<MemberEntity> findByUsername(String username);

    Optional<MemberEntity> findByKinderNoAndRoleNoAndStatus(Long kinderNo, int role, Integer status);
}
