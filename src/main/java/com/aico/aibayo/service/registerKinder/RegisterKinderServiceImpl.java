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

    @Override
    public RegisterKinderDto getByKinderNo(Long kinderNo) {
        return registerKinderRepository.findById(kinderNo)
                .map(RegisterKinderDto::toDto)
                .orElseThrow(() -> new EntityNotFoundException("엔티티를 찾을 수 없습니다."));
    }
}
