package com.aico.aibayo.service.kid;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.AcceptTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.kid.KidSearchCondition;
import com.aico.aibayo.entity.AcceptLogEntity;
import com.aico.aibayo.entity.ClassKidEntity;
import com.aico.aibayo.entity.KidEntity;
import com.aico.aibayo.repository.AcceptLogRepository;
import com.aico.aibayo.repository.classKid.ClassKidRepository;
import com.aico.aibayo.repository.kid.KidRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KidServiceImpl implements KidService {
    private final KidRepository kidRepository;
    private final ClassKidRepository classKidRepository;
    private final AcceptLogRepository acceptLogRepository;

    @Override
    public List<KidDto> getByKinderNo(Long kinderNo) {
        List<KidEntity> kidEntities =
                kidRepository.findAllByKinderNoAndDischargeFlagEquals(kinderNo, BooleanEnum.FALSE.getBool());

        return kidEntities.stream()
                          .map(KidDto :: toDto)
                          .collect(Collectors.toList());
    }

    @Override
    public List<KidDto> getByMemberId(Long id) {
        return kidRepository.findAllByMemberId(id);
    }

    @Override
    public List<KidDto> getAllByClassNoAndAcceptStatus(KidSearchCondition condition) {
        return kidRepository.findAllByClassNoAndAcceptStatus(condition);
    }

    @Override
    public List<KidDto> getAllWithParentByClassNoAndAcceptStatus(KidSearchCondition condition) {
        return kidRepository.findAllWithParentByClassNoAndAcceptStatus(condition);
    }

    @Override
    public List<KidDto> getAllWithInviteByClassNoAndAcceptStatus(KidSearchCondition condition) {
        return kidRepository.findAllWithInviteByClassNoAndAcceptStatus(condition);
    }

    @Override
    public KidDto getByKidNo(Long kidNo) {
        return KidDto.toDto(Objects.requireNonNull(kidRepository.findById(kidNo).orElse(null)));
    }

    @Override
    @Transactional
    public KidDto insertKid(KidDto kidDto) {
        // 원생 insert
        KidEntity kidEntity = KidEntity.builder()
                .kinderNo(kidDto.getKinderNo())
                .kidName(kidDto.getKidName())
                .kidBirth(kidDto.getKidBirth())
                .kidGender(kidDto.getKidGender())
                .admissionDate(LocalDateTime.now())
                .dischargeFlag(BooleanEnum.FALSE.getBool())
                .build();
        KidEntity insertedKid = kidRepository.save(kidEntity);

        // 반_원생 insert
        for (Long classNo : kidDto.getClassNoList()) {
            AcceptLogEntity acceptLogEntity = AcceptLogEntity.builder()
                    .acceptType(AcceptTypeEnum.CLASS_KID.getType())
                    .acceptStatus(AcceptStatusEnum.ACCEPT.getStatus())
                    .acceptDate(LocalDateTime.now())
                    .build();
            AcceptLogEntity insertedAcceptLog = acceptLogRepository.save(acceptLogEntity);

            ClassKidEntity classKidEntity = ClassKidEntity.builder()
                    .classNo(classNo)
                    .kidNo(insertedKid.getKidNo())
                    .acceptNo(insertedAcceptLog.getAcceptNo())
                    .build();
            classKidRepository.save(classKidEntity);
        }

        return KidDto.toDto(insertedKid);
    }

    @Override
    public KidDto updateKid(KidDto kidDto) {
        KidEntity target = kidRepository.findById(kidDto.getKidNo()).orElse(null);

        if (target == null) { return null; }

        // 원생 기본정보 업데이트
        if (kidDto.getKidName() != null) { target.setKidName(kidDto.getKidName()); }
        if (kidDto.getKidGender() != null) { target.setKidGender(kidDto.getKidGender()); }
        if (kidDto.getKidBirth() != null) { target.setKidBirth(kidDto.getKidBirth()); }

        target.setModifyDate(LocalDateTime.now());

        return KidDto.toDto(kidRepository.save(target));
    }

    @Override
    @Transactional
    public void updateClassKid(KidDto kidDto) {
        // 반_원생 관계 업데이트
        // 1. 승인이력 insert
        // 2. 반_원생 insert
        if (kidDto.getClassNo() != null) {
            AcceptLogEntity acceptLogEntity = AcceptLogEntity.builder()
                    .acceptType(AcceptTypeEnum.CLASS_KID.getType())
                    .acceptStatus(AcceptStatusEnum.ACCEPT.getStatus())
                    .acceptDate(LocalDateTime.now())
                    .acceptRegDate(LocalDateTime.now())
                    .acceptDeleteFlag(BooleanEnum.FALSE.getBool())
                    .build();
            AcceptLogEntity saved = acceptLogRepository.save(acceptLogEntity);

            ClassKidEntity classKidEntity = ClassKidEntity.builder()
                    .classNo(kidDto.getClassNo())
                    .kidNo(kidDto.getKidNo())
                    .acceptNo(saved.getAcceptNo())
                    .build();

            classKidRepository.save(classKidEntity);
        }

    }

    @Override
    public KidDto updateParentKid(KidDto kidDto) {
        if (kidDto.getParentKidAcceptNo() != null) {
            acceptLogRepository.findById(kidDto.getParentKidAcceptNo()).ifPresent(target -> {
                target.setAcceptStatus(AcceptStatusEnum.ACCEPT.getStatus());
                target.setAcceptModifyDate(LocalDateTime.now());
                target.setAcceptDate(LocalDateTime.now());

                acceptLogRepository.save(target);
            });

            return kidDto;
        }
        return null;
    }

    @Override
    public KidDto deleteKid(KidDto kidDto) {

        // 관계 삭제일 경우
        if (kidDto.getAcceptNo() != null) {
            AcceptLogEntity target = acceptLogRepository.findById(kidDto.getAcceptNo()).orElse(null);
            if (target != null) {
                target.setAcceptStatus(AcceptStatusEnum.DELETE.getStatus());
                target.setAcceptDeleteFlag(BooleanEnum.TRUE.getBool());
                target.setAcceptDeleteDate(LocalDateTime.now());

                acceptLogRepository.save(target);
                return kidDto;
            }
        }

        // 보호자_원생 관계 승인거부
        if (kidDto.getParentKidAcceptNo() != null) {
            acceptLogRepository.findById(kidDto.getParentKidAcceptNo()).ifPresent(target -> {
                target.setAcceptStatus(AcceptStatusEnum.REJECT.getStatus());
                target.setAcceptDeleteDate(LocalDateTime.now());
                target.setAcceptDeleteFlag(BooleanEnum.TRUE.getBool());

                acceptLogRepository.save(target);
            });

            return kidDto;
        }


        // 원생 퇴소
        if (kidDto.getKidNo() != null) {
            KidEntity target = kidRepository.findById(kidDto.getKidNo()).orElse(null);
            if (target != null) {
                target.setDischargeFlag(BooleanEnum.TRUE.getBool());
                target.setDischargeDate(LocalDateTime.now());

                KidEntity deleted = kidRepository.save(target);
                return KidDto.toDto(deleted);
            }
        }

        return null;
    }

    @Override
    public List<KidDto> getAllByParent(Long id) {
        return kidRepository.findAllByParent(id);
    }

}
