package com.aico.aibayo.jwt;

import com.aico.aibayo.common.MemberStatusEnum;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.exception.MemberNotFoundException;
import com.aico.aibayo.repository.member.MemberRepository;
import com.aico.aibayo.service.member.MemberServiceImpl;
import groovy.util.logging.Slf4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomMemberStatusFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepository;
    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getParameter("username");
        String requestUri = request.getRequestURI();

        // 회원가입 URL은 필터에서 제외
//        if (requestUri.equals("/member/finalSignUp") || requestUri.equals("/member/signUpInviteTeacher") || requestUri.equals("/member/signUpKinder")) {
//            log.info("회원가입 요청을 필터에서 제외: {}", requestUri);
//            filterChain.doFilter(request, response);
//            return;
//        }



        // 로그인 시 여기까지 안옴 status 체크를 안함
        log.info("CustomMemberStatusFilter: username = {}", username);
        if (username != null) {
            MemberEntity member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new MemberNotFoundException("username으로 검색한 member 값이 없습니다."));;

                    log.info("member : {}", member);
                    log.info("MemberStatusEnum.ACTIVE.getStatus() : {}", MemberStatusEnum.ACTIVE.getStatus());
            if (member != null && member.getStatus() != MemberStatusEnum.ACTIVE.getStatus()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자의 승인이 필요합니다.");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
