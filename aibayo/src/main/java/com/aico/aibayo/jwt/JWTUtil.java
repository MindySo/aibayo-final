package com.aico.aibayo.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JWTUtil {
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }



    @Value("${jwt.access-token-expiration-minutes}")
    private int originMinutes;

    private int accessTokenExpirationMinutes;

    @PostConstruct
    public void init() {
        this.accessTokenExpirationMinutes = this.originMinutes * 24;
    }



    // jwt 검증
    public String getUsername(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    public Boolean isExpired(String token) {
        try {
            JwtParserBuilder jwtParserBuilder = Jwts.parser().setSigningKey(secretKey);
            Claims claims = jwtParserBuilder.build().parseClaimsJws(token).getBody();
            Date issuedAt = claims.getIssuedAt();
            Date expiration = claims.getExpiration();
            boolean isExpired = expiration.before(new Date());

            log.info("Token details: issuedAt={}, expiration={}", issuedAt, expiration);
            return isExpired;
        } catch (Exception e) {
            log.error("Error checking if JWT is expired: {}", e.getMessage());
            return true;
        }
    }


    // verifyWith(secretKey) : 우리 서버에서 생성한 키인지 확인
    // parseSignedClaims(token).getPayload() : 특정 데이터 가져오기
    // get("username", String.class) : String 형식으로 유저이름 가져오기

    // jwt 생성
    public String createJwt(String username, String role, Long expiredMs) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);
        log.info("Creating JWT: username={}, role={}, issuedAt={}, expiration={}", username, role, new Date(), expirationDate);

        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
}
