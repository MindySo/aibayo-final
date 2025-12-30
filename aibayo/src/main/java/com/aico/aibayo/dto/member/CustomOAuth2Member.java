package com.aico.aibayo.dto.member;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class CustomOAuth2Member implements OAuth2User {
    private final MemberDto memberDto;
    private boolean newMember;

    @Override
    public Map<String, Object> getAttributes() {
        return null; // Google과 Naver의 응답 형식이 다르기 때문에 기본적으로 null을 반환
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return memberDto.getName();
    }

    public String getUsername() {
        return memberDto.getUsername();
    }
}
