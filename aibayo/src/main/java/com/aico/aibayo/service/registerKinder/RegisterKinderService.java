package com.aico.aibayo.service.registerKinder;

import com.aico.aibayo.dto.RegisterKinderDto;
import org.springframework.stereotype.Service;

@Service
public interface RegisterKinderService {
    RegisterKinderDto getByKinderNo(Long kinderNo);
}
