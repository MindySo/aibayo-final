package com.aico.aibayo.jwt;

import com.aico.aibayo.service.member.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

//@RequiredArgsConstructor
//public class CustomLogoutFilter extends GenericFilterBean {
//    private final JWTUtil jwtUtil;
//    private final TokenService tokenService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
//    }
//
//    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        // path와 method 확인
//        String requestUri = request.getRequestURI();
//        if (!requestUri.matches("^\\/logout$")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String requestMethod = request.getMethod();
//        if (!requestMethod.equals("POST")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // JWT 토큰 가져오기
//        String jwtToken = null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals("jwt-token")) {
//                    jwtToken = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        // JWT 토큰 null 체크
//        if (jwtToken == null) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        // 토큰 유효성 검사
//        try {
//            jwtUtil.isExpired(jwtToken);
//        } catch (ExpiredJwtException e) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        // DB에 저장되어 있는지 확인
//        if (!tokenService.tokenExists(jwtToken)) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//
//        // 로그아웃 진행
//        // 토큰 DB에서 제거
//        tokenService.deleteToken(jwtToken);
//
//        // 쿠키 값 제거
//        Cookie cookie = new Cookie("jwt", null);
//        cookie.setMaxAge(0);
//        cookie.setPath("/");
//
//        response.addCookie(cookie);
//        response.setStatus(HttpServletResponse.SC_OK);
//    }
//}


@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken == null || jwtUtil.isExpired(jwtToken) || !tokenService.tokenExists(jwtToken)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        tokenService.deleteToken(jwtToken);

        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}