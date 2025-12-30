package com.aico.aibayo.service.member;

import com.aico.aibayo.dto.member.CustomMemberDetails;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.exception.MemberNotFoundException;
import com.aico.aibayo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomMemberDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username으로 검색한 member 값이 없습니다."));

        return new CustomMemberDetails(memberEntity);
    }
}
