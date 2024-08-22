package com.aico.aibayo.service.kid;

import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.kid.KidSearchCondition;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface KidService {
    List<KidDto> getByKinderNo(Long kinderNo);
    List<KidDto> getByMemberId(Long id);
    List<KidDto> getAllByClassNoAndAcceptStatus(KidSearchCondition condition);
    List<KidDto> getAllWithParentByClassNoAndAcceptStatus(KidSearchCondition condition);
    List<KidDto> getAllWithInviteByClassNoAndAcceptStatus(KidSearchCondition condition);
    KidDto getByKidNo(Long kidNo);
    KidDto insertKid(KidDto kidDto);
    KidDto updateKid(KidDto kidDto);
    void updateClassKid(KidDto kidDto);
    KidDto updateParentKid(KidDto kidDto);
    KidDto deleteKid(KidDto kidDto);
    List<KidDto> getAllByParent(Long id);
}
