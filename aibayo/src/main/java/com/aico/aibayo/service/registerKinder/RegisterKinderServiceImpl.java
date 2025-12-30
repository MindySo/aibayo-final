package com.aico.aibayo.service.registerKinder;

import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.repository.RegisterKinderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterKinderServiceImpl implements RegisterKinderService {
    private final RegisterKinderRepository registerKinderRepository;

//    @Override
//    public RegisterKinderDto getByKinderNo(Long kinderNo) {
//        return registerKinderRepository.findById(kinderNo)
//                .map(RegisterKinderDto::toDto)
//                .orElseThrow(() -> new EntityNotFoundException("엔티티를 찾을 수 없습니다."));
//    }
    @Override
    public RegisterKinderDto getByKinderNo(Long kinderNo) {
        if (kinderNo == null) {
            log.error("kinderNo 값이 null입니다.");
            throw new IllegalArgumentException("유효한 kinderNo 값이 필요합니다.");
        }
        return registerKinderRepository.findById(kinderNo)
                .map(RegisterKinderDto::toDto)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 유치원 정보를 찾을 수 없습니다."));
    }
}
