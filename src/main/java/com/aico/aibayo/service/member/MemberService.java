package com.aico.aibayo.service.member;

import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    List<MemberDto> getAllByKidNo(Long kidNo);
    MemberDto getByIdAndKidNo(MemberSearchCondition condition);
    MemberDto findByUsername(String username);

    boolean checkPassword(String username, String password);

    void updatePassword(String username, String newPassword);

    MemberDto getByUsernameWithParentKid(String username);
    MemberDto getByUsernameWithParentKid(MemberSearchCondition condition);

    void deleteMember(String username, String role);

    void signUpProcess(MemberDto memberDto);

}
