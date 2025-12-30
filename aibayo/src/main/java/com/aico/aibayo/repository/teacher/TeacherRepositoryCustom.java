package com.aico.aibayo.repository.teacher;

import com.aico.aibayo.dto.teacher.TeacherSearchCondition;
import com.aico.aibayo.dto.teacher.TeacherDto;

import java.util.List;

public interface TeacherRepositoryCustom {

    List<TeacherDto> findAcceptedTeacherByKinderNoAndClassNo(TeacherSearchCondition condition);
    List<TeacherDto> findTeacherByKinderNo(TeacherSearchCondition condition);
    List<TeacherDto> findInvitedTeacherByKinderNo(TeacherSearchCondition condition);
    TeacherDto findTeacherById(Long id);
}
