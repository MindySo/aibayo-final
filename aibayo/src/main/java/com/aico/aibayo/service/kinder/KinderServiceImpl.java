package com.aico.aibayo.service.kinder;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.MemberRoleEnum;
import com.aico.aibayo.common.MemberStatusEnum;
import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.kinder.KinderDto;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.entity.RegisterKinderEntity;
import com.aico.aibayo.repository.member.MemberRepository;
import com.aico.aibayo.repository.settingKinder.KinderRepository;
import com.aico.aibayo.repository.settingKinder.SettingMenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KinderServiceImpl implements KinderService {
    private final KinderRepository kinderRepository;
    private final MemberRepository memberRepository;

    @Override
    public Optional<RegisterKinderEntity> findByKinderNo(Long kinderNo) {
        return kinderRepository.findById(kinderNo);
    }

    @Override
    public List<RegisterKinderDto> getAllKinder() {
        List<RegisterKinderEntity> kinderEntities =
                kinderRepository.findAllByDeleteFlag(BooleanEnum.FALSE.getBool());
        return kinderEntities.stream().map(RegisterKinderDto::toDto).collect(Collectors.toList());
    }

    @Override
    public RegisterKinderDto getKinderById(Long kinderNo) {
        return
        kinderRepository.findById(kinderNo).map(target->{
            RegisterKinderDto dto = RegisterKinderDto.toDto(target);
            memberRepository.findByKinderNoAndRoleNoAndStatus(
                    dto.getKinderNo(), MemberRoleEnum.PRINCIPAL.getRole(), MemberStatusEnum.ACTIVE.getStatus())
                    .ifPresent(member -> dto.setPrincipalName(member.getName()));

            return dto;
        }).orElse(null);
    }

    @Override
    public RegisterKinderDto getKinderDtoById(Long kinderNo) {
        RegisterKinderEntity target = kinderRepository.findById(kinderNo).orElse(null);
        return RegisterKinderDto.toDto(target);
    }

    @Override
    @Transactional
    public RegisterKinderDto insertKinder(KinderDto kinderDto) {
        RegisterKinderEntity kinderentity = RegisterKinderEntity.builder()
                .kinderOpenTime(kinderDto.getKinderOpenTime())
                .kinderCloseTime(kinderDto.getKinderCloseTime())
                .kinderName(kinderDto.getKinderName())
                .kinderPostCode(kinderDto.getKinderPostCode())
                .kinderLocNo(kinderDto.getKinderLocNo())
                .kinderMidNo(kinderDto.getKinderMidNo())
                .kinderEndNo(kinderDto.getKinderEndNo())
                .kinderAddr(kinderDto.getKinderAddr())
                .kinderAddrDetail(kinderDto.getKinderAddrDetail())
                .kinderRegDate(LocalDateTime.now())
                .sidoList(kinderDto.getSidoList())
                .sggList(kinderDto.getSggList())
                .deleteFlag(BooleanEnum.FALSE.getBool())
                .build();
        RegisterKinderEntity saveKinder = kinderRepository.save(kinderentity);
        return RegisterKinderDto.toDto(saveKinder);
    }

    @Override
    @Transactional
    public void updateKinder(KinderDto kinderDto) {
        RegisterKinderEntity registerKinderEntity =
            kinderRepository.findById(kinderDto.getKinderNo()).orElse(null);
            assert registerKinderEntity != null;
            registerKinderEntity.setKinderPostCode(kinderDto.getKinderPostCode());
            registerKinderEntity.setKinderAddr(kinderDto.getKinderAddr());
            registerKinderEntity.setKinderAddrDetail(kinderDto.getKinderAddrDetail());
            registerKinderEntity.setKinderName(kinderDto.getKinderName());
            registerKinderEntity.setKinderLocNo(kinderDto.getKinderLocNo());
            registerKinderEntity.setKinderMidNo(kinderDto.getKinderMidNo());
            registerKinderEntity.setKinderEndNo(kinderDto.getKinderEndNo());
            registerKinderEntity.setKinderModifyDate(LocalDateTime.now());
            registerKinderEntity.setKinderOpenTime(kinderDto.getKinderOpenTime());
            registerKinderEntity.setKinderCloseTime(kinderDto.getKinderCloseTime());
            registerKinderEntity.setSggList(kinderDto.getSggList());
            registerKinderEntity.setSidoList(kinderDto.getSidoList());
        kinderRepository.save(registerKinderEntity);
    }

    @Override
    public void deleteKinder(Long kinderNo) {

    }
}
