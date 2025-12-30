package com.aico.aibayo.repository.classTeacher;

import com.aico.aibayo.dto.classTeacher.ClassTeacherDto;

import java.util.List;

public interface ClassTeacherRepositoryCustom {
    List<ClassTeacherDto> findAllByClassNo(Long classNo);
}
