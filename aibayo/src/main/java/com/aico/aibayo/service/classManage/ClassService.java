package com.aico.aibayo.service.classManage;

import com.aico.aibayo.dto.ClassDto;
import java.util.List;
import java.util.Optional;

import com.aico.aibayo.dto.classKid.ClassKidDto;
import com.aico.aibayo.dto.classTeacher.ClassTeacherDto;
import com.aico.aibayo.entity.ClassEntity;
import org.springframework.stereotype.Service;

@Service
public interface ClassService {
    List<ClassDto> getByKinderNo(Long kinderNo);
    List<ClassDto> getByMemberId(Long id);
    List<ClassDto> getAllByKidNo(Long kidNo);

    List<ClassDto> getAllByKinderNo(Long kinderNo);
    List<ClassDto> getAddableClassByKinderNo(Long kinderNo);
    List<ClassDto> getClassByKinderNoAndTeacherId(Long kinderNo, Long id);

    List<ClassEntity> getClassList(Long kinderNo);

    List<ClassKidDto> getClassKid(Long classNo);

    List<ClassTeacherDto> getClassTeacher(Long classNo);

    void updateClassName(Long classNo, String newClassName);

    void deleteClass(Long classNo);

    void createClass(String className, Long kinderNo);
}
