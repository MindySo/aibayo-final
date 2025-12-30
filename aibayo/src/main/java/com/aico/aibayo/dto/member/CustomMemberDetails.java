package com.aico.aibayo.dto.member;

import com.aico.aibayo.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomMemberDetails implements UserDetails {
    private final MemberEntity memberEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return memberEntity.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return memberEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return memberEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // member의 status가 2(INACTIVE)일 경우 계정을 잠금 상태로 처리
        return memberEntity.getStatus() != 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // status가 2(INACTIVE)일 경우 계정을 비활성화 상태로 처리
        return memberEntity.getStatus() == 1;
    }
}
