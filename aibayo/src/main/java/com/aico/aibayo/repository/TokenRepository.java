package com.aico.aibayo.repository;

import com.aico.aibayo.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    boolean existsByToken(String token);
    void deleteByToken(String token);
    TokenEntity findByToken(String token);
}