package com.aico.aibayo.repository.member;

import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {
    List<MemberDto> findAllByKidNo(Long kidNo);

    Optional<MemberDto> findByIdAndKidNo(MemberSearchCondition condition);
    Optional<MemberDto> findByUsernameWithParentKid(String username);
    Optional<MemberDto> findByUsernameWithParentKid(MemberSearchCondition condition);
    Optional<MemberDto> findByUsernameWithClassTeacher(String username);

    MemberDto findDtoById(Long id);

}
