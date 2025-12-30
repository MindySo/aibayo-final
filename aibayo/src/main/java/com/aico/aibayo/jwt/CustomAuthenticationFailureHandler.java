package com.aico.aibayo.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 실패 메시지를 alert로 표시하고 현재 페이지 유지

        System.out.println("로그인 실패");
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(
                "<html><head>" +
                        "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css'>" +
                        "<script src='https://cdn.jsdelivr.net/npm/sweetalert2@11'></script>" +
                        "</head><body>" +
                        "<script>" +
                        "Swal.fire({" +
                        "  icon: 'error'," +  // 에러 아이콘 사용
                        "  title: '로그인 실패'," +  // 제목 설정
                        "  text: '로그인에 실패했습니다. 아이디와 비밀번호를 확인하세요.'," +  // 메시지 내용
                        "  confirmButtonText: '확인'" +  // 확인 버튼 텍스트
                        "}).then(function() {" +
                        "  history.back();" +  // 확인 버튼을 누르면 이전 페이지로 이동
                        "});" +
                        "</script>" +
                        "</body></html>"
        );
    }
}
