package com.aico.aibayo.jwt;

import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.repository.member.MemberRepository;
import com.aico.aibayo.service.member.TokenService;
import jakarta.servlet.http.Cookie;
import com.aico.aibayo.dto.member.CustomMemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String username = obtainUsername(request);
            String password = obtainPassword(request);

            // 사용자의 인증 정보를 바탕으로 인증을 시도합니다.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 인증이 성공한 후, 사용자의 상태를 확인합니다.
            CustomMemberDetails customMemberDetails = (CustomMemberDetails) authentication.getPrincipal();

            // 사용자의 상태가 INACTIVE(0)라면, 계정이 잠겨있다는 예외를 발생시킵니다.
            if (!customMemberDetails.isAccountNonLocked()) {
                log.warn("계정이 비활성화된 상태입니다: {}", username);
                throw new AuthenticationException("계정이 비활성화되었습니다.") {
                };
            }

            return authentication;
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", obtainUsername(request));

            try {
                // 실패 핸들러를 명시적으로 호출하여 실패 처리를 수행
                super.unsuccessfulAuthentication(request, response, e);
            } catch (IOException | ServletException ex) {
                // 예외 처리 - 로그를 추가하거나 적절히 처리
                log.error("Error during unsuccessful authentication handling", ex);
            }

            // 예외를 다시 던지지 않음
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomMemberDetails customMemberDetails = (CustomMemberDetails) authResult.getPrincipal();
        String username = customMemberDetails.getUsername();
        String role = customMemberDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("ROLE_USER");

        MemberEntity memberEntity = memberRepository.findByUsername(username).orElse(null);
        if (memberEntity != null) {
            memberEntity.setLatestLogDate(LocalDateTime.now());
            log.info("loginfilter setLatestLogDate : {}", LocalDateTime.now());
            memberRepository.save(memberEntity);
        }

        String token = jwtUtil.createJwt(username, role, 86400000L);

        tokenService.saveToken(token);

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400);

        response.addCookie(jwtCookie);

        if (role.equals("ROLE_ADMIN")) {
            response.sendRedirect("/main/siteAdmin");
        } else if (role.equals("ROLE_PRINCIPAL") || role.equals("ROLE_TEACHER")) {
            response.sendRedirect("/main/admin");
        } else if (role.equals("ROLE_USER")) {
            response.sendRedirect("/main/user");
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}