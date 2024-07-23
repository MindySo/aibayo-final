package com.aico.aibayo.service.classManage;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.entity.ClassEntity;
import com.aico.aibayo.repository.classManage.ClassRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService{
    private final ClassRepository classRepository;

    @Override
    public List<ClassDto> getByKinderNo(Long kinderNo) {
        List<ClassEntity> classEntities = classRepository.findAllByKinderNoAndClassDeleteFlagEquals(
                kinderNo, BooleanEnum.FALSE.getBool());

        return classEntities.stream()
                            .map(ClassDto::toDto)
                            .collect(Collectors.toList());
    }

    @Override
    public List<ClassDto> getByMemberId(Long id) {
        return classRepository.findAllByMemberId(id);
    }
}
