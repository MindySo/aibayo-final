package com.aico.aibayo.jwt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

    @Test
    void testJwtCreationAndExpiration() {
        JWTUtil jwtUtil = new JWTUtil("vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb");

        // 24시간 유효 토큰 생성
        long validityInMilliseconds = 86400000L; // 24시간
        String token = jwtUtil.createJwt("username", "role", validityInMilliseconds);

        // 생성된 토큰 출력
        System.out.println("Generated Token: " + token);

        // 토큰 만료 여부 확인
        boolean isExpired = jwtUtil.isExpired(token);
        System.out.println("Is Token Expired? " + isExpired);

        // 테스트 검증
        assertFalse(isExpired, "Token should not be expired immediately after creation");
    }
}
