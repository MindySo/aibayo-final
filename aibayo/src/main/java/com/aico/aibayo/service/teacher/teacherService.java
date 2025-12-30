package com.aico.aibayo.service.teacher;

import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.teacher.TeacherSearchCondition;
import com.aico.aibayo.dto.teacher.TeacherDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface teacherService {
    List<TeacherDto> getAcceptedTeacherByKinderNoAndClassNo(TeacherSearchCondition condition);
    List<TeacherDto> getTeacherByKinderNo(TeacherSearchCondition condition);
    List<TeacherDto> getInvitedTeacherByKinderNo(TeacherSearchCondition condition);
    TeacherDto getTeacherById(Long id);
    void updateClassTeacher(List<Long> newClassIds, List<Long> oldClassAcceptNos, Long id);
    MemberDto updateTeacher(Map<String, Number> requestBody);
    MemberDto deleteTeacher(Map<String, Object> requestBody);

}
