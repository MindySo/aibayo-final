package com.aico.aibayo.service.classManage;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.classKid.ClassKidDto;
import com.aico.aibayo.dto.classTeacher.ClassTeacherDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.entity.ClassEntity;
import com.aico.aibayo.entity.KidEntity;
import com.aico.aibayo.repository.classKid.ClassKidRepository;
import com.aico.aibayo.repository.classKid.ClassKidRepositoryCustom;
import com.aico.aibayo.repository.classManage.ClassRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.aico.aibayo.repository.classTeacher.ClassTeacherRepository;
import com.aico.aibayo.repository.kid.KidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService{
    private final ClassRepository classRepository;
    private final ClassKidRepository classKidRepository;
    private final ClassTeacherRepository classTeacherRepository;

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

    @Override
    public List<ClassDto> getAllByKidNo(Long kidNo) {
        return classRepository.findAllByKidNo(kidNo);
    }

    @Override
    public List<ClassDto> getAllByKinderNo(Long kinderNo) {return classRepository.findAllByKinderNo(kinderNo); }

    @Override
    public List<ClassDto> getAddableClassByKinderNo(Long kinderNo) {return classRepository.findAddableClassByKinderNo(kinderNo); }

    @Override
    public List<ClassDto> getClassByKinderNoAndTeacherId(Long kinderNo, Long id) {return classRepository.findClassByKinderNoAndTeacherId(kinderNo, id); }

    @Override
    public List<ClassEntity> getClassList(Long kinderNo) {
        return classRepository.findAllByKinderNoAndClassDeleteFlagEquals(kinderNo, BooleanEnum.FALSE.getBool());
    }

    @Override
    public List<ClassKidDto> getClassKid(Long classNo) {
        return classKidRepository.findAllByClassNo(classNo);
    }

    @Override
    public List<ClassTeacherDto> getClassTeacher(Long classNo) {
        return classTeacherRepository.findAllByClassNo(classNo);
    }

    @Override
    public void updateClassName(Long classNo, String newClassName) {
        ClassEntity classEntity = classRepository.findByClassNo(classNo);
        classEntity.setClassName(newClassName);
        classEntity.setClassModifyDate(LocalDateTime.now());
        ClassEntity save = classRepository.save(classEntity);
        System.out.println("save.getClassName()" + save.getClassName());
    }

    @Override
    public void deleteClass(Long classNo) {
        ClassEntity classEntity = classRepository.findByClassNo(classNo);
        classEntity.setClassDeleteFlag(BooleanEnum.TRUE.getBool());
        classEntity.setClassDeleteDate(LocalDateTime.now());
        classRepository.save(classEntity);
    }

    @Override
    public void createClass(String className, Long kinderNo) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setClassName(className);
        classEntity.setKinderNo(kinderNo);
        classEntity.setClassRegDate(LocalDateTime.now());
        classEntity.setClassDeleteFlag(BooleanEnum.FALSE.getBool());
        classRepository.save(classEntity);
    }


}
