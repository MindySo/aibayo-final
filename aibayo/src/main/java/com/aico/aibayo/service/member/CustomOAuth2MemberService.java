package com.aico.aibayo.service.member;

import com.aico.aibayo.dto.member.*;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.exception.MemberNotFoundException;
import com.aico.aibayo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomOAuth2MemberService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        MemberEntity existData = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("username으로 검색한 member 값이 없습니다."));;
        if (existData == null) {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setUsername(username);
            memberEntity.setName(oAuth2Response.getName());
            memberEntity.setRole("ROLE_USER"); // 기본 권한 설정
            memberEntity.setRegDate(LocalDateTime.now());
            memberEntity.setLatestLogDate(LocalDateTime.now());

            memberRepository.save(memberEntity);

            MemberDto memberDto = new MemberDto();
            memberDto.setName(oAuth2Response.getName());
            memberDto.setUsername(username);
            memberDto.setRole("ROLE_USER");
            memberDto.setRegDate(LocalDateTime.now());
            memberDto.setLatestLogDate(LocalDateTime.now());

            CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberDto);
            customOAuth2Member.setNewMember(true); // 새로운 사용자 여부 설정
            return customOAuth2Member;
        } else {
            existData.setName(oAuth2Response.getName());
            memberRepository.save(existData);

            MemberDto memberDto = new MemberDto();
            memberDto.setName(oAuth2Response.getName());
            memberDto.setUsername(username);
            memberDto.setRole("ROLE_USER");
            memberDto.setRegDate(LocalDateTime.now());
            memberDto.setLatestLogDate(LocalDateTime.now());

            CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberDto);
            customOAuth2Member.setNewMember(false); // 기존 사용자 여부 설정
            return customOAuth2Member;
        }
    }
}