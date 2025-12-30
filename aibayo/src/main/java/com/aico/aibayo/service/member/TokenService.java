package com.aico.aibayo.service.member;

import com.aico.aibayo.entity.TokenEntity;
import com.aico.aibayo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(String token) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(tokenEntity);
    }

    public boolean tokenExists(String token) {
        return tokenRepository.existsByToken(token);
    }

    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    public void invalidateToken(String token) {
        TokenEntity tokenEntity = tokenRepository.findByToken(token);
        if (tokenEntity != null) {
//            tokenEntity.setToken(null); // 토큰 무효화
//            tokenRepository.save(tokenEntity);
            tokenRepository.delete(tokenEntity);
        }
    }
}