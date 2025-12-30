package com.aico.aibayo.service.kinder;

import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.kinder.KinderDto;
import com.aico.aibayo.entity.RegisterKinderEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface KinderService {

    Optional<RegisterKinderEntity> findByKinderNo(Long kinderNo);

    List<RegisterKinderDto> getAllKinder();

    RegisterKinderDto getKinderById(Long kinderNo);

    RegisterKinderDto getKinderDtoById(Long kinderNo);

    RegisterKinderDto insertKinder(KinderDto kinderDto);

    void updateKinder(KinderDto kinderDto);

    void deleteKinder(Long kinderNo);
}
