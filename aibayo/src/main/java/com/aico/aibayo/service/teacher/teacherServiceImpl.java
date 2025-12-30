package com.aico.aibayo.service.teacher;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.AcceptTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.teacher.TeacherSearchCondition;
import com.aico.aibayo.dto.teacher.TeacherDto;
import com.aico.aibayo.entity.AcceptLogEntity;
import com.aico.aibayo.entity.ClassTeacherEntity;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.repository.AcceptLogRepository;
import com.aico.aibayo.repository.classTeacher.ClassTeacherRepository;
import com.aico.aibayo.repository.member.MemberRepository;
import com.aico.aibayo.repository.teacher.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class teacherServiceImpl implements teacherService{
    private final TeacherRepository teacherRepository;
    private final AcceptLogRepository acceptLogRepository;
    private final ClassTeacherRepository classTeacherRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<TeacherDto> getAcceptedTeacherByKinderNoAndClassNo(TeacherSearchCondition condition) {
        return teacherRepository.findAcceptedTeacherByKinderNoAndClassNo(condition);
    }

    @Override
    public List<TeacherDto> getTeacherByKinderNo(TeacherSearchCondition condition) {
        return teacherRepository.findTeacherByKinderNo(condition);
    }

    @Override
    public List<TeacherDto> getInvitedTeacherByKinderNo(TeacherSearchCondition condition) {
        return teacherRepository.findInvitedTeacherByKinderNo(condition);
    }

    @Override
    public TeacherDto getTeacherById(Long id) {
        return teacherRepository.findTeacherById(id);
    }

    @Override
    @Transactional
    public void updateClassTeacher(List<Long> newClassIds, List<Long> oldClassAcceptNos, Long id) {
        for(Long newClassId : newClassIds) {
            // 승인이력 insert
            AcceptLogEntity acceptLogEntity = AcceptLogEntity.builder()
                    .acceptType(AcceptTypeEnum.CLASS_TEACHER.getType())
                    .acceptStatus(AcceptStatusEnum.ACCEPT.getStatus())
                    .acceptDate(LocalDateTime.now())
                    .acceptRegDate(LocalDateTime.now())
                    .acceptDeleteFlag(BooleanEnum.FALSE.getBool())
                    .build();
            AcceptLogEntity saved = acceptLogRepository.save(acceptLogEntity);

            // 새로운 반_선생 insert
            ClassTeacherEntity classTeacherEntity = ClassTeacherEntity.builder()
                    .classNo(newClassId)
                    .classTeacherId(id)
                    .acceptNo(saved.getAcceptNo())
                    .build();
            classTeacherRepository.save(classTeacherEntity);
        }

        // 사라질 반_선생 update
        for(Long oldClassAcceptNo : oldClassAcceptNos){
            acceptLogRepository.findById(oldClassAcceptNo).ifPresent(acceptLogEntity -> {
                acceptLogEntity.setAcceptStatus(AcceptStatusEnum.DELETE.getStatus());
                acceptLogEntity.setAcceptDeleteDate(LocalDateTime.now());
                acceptLogEntity.setAcceptDeleteFlag(BooleanEnum.TRUE.getBool());
            });
        }
    }

    @Override
    public MemberDto updateTeacher(Map<String, Number> requestBody) {
        Long id = requestBody.get("id").longValue();
        System.out.println("id : " + id);
        MemberDto memberDto = null;

        if (requestBody.get("teacherKinderAcceptNo") != null) {
            Long kinderAcceptNo = requestBody.get("teacherKinderAcceptNo").longValue();

            AcceptLogEntity target = acceptLogRepository.findById(kinderAcceptNo).orElse(null);
            if (target != null) {
                target.setAcceptStatus(AcceptStatusEnum.ACCEPT.getStatus());
                target.setAcceptModifyDate(LocalDateTime.now());

                acceptLogRepository.save(target);
                memberDto = memberRepository.findDtoById(id);
            }
        }

        return memberDto;
    }

    @Override
    public MemberDto deleteTeacher(Map<String, Object> requestBody) {

        Long id = ((Number) requestBody.get("id")).longValue();
        log.info("id : " + id);
        MemberDto memberDto = null;

        // class_teacher 삭제
        if (requestBody.get("classAcceptNos") != null) {
            List<Integer> classAcceptNosInt = (List<Integer>) requestBody.get("classAcceptNos");
            log.info("classAcceptNos : " + classAcceptNosInt);
            List<Long> classAcceptNos = classAcceptNosInt.stream()
                    .map(Integer::longValue)
                    .collect(Collectors.toList());

            for(long classAcceptNo : classAcceptNos){
                AcceptLogEntity target = acceptLogRepository.findById(classAcceptNo).orElse(null);
                if (target != null) {
                    target.setAcceptStatus(AcceptStatusEnum.DELETE.getStatus());
                    target.setAcceptDeleteFlag(BooleanEnum.TRUE.getBool());
                    target.setAcceptDeleteDate(LocalDateTime.now());

                    acceptLogRepository.save(target);
                    memberDto = memberRepository.findDtoById(id);
                }
            }
        }

        // teacher_kinder 삭제
        if (requestBody.get("kinderAcceptNo") != null) {
            Long kinderAcceptNo = ((Number) requestBody.get("kinderAcceptNo")).longValue();
            log.info("kinderAcceptNo : " + kinderAcceptNo);

            AcceptLogEntity target = acceptLogRepository.findById(kinderAcceptNo).orElse(null);
            if (target != null) {
                target.setAcceptStatus(AcceptStatusEnum.DELETE.getStatus());
                target.setAcceptDeleteFlag(BooleanEnum.TRUE.getBool());
                target.setAcceptDeleteDate(LocalDateTime.now());

                acceptLogRepository.save(target);
                memberDto = memberRepository.findDtoById(id);
            }
        }

        // member에서 유치원 번호 삭제
        if (requestBody.get("id") != null) {
            MemberEntity target = teacherRepository.findById(id).orElse(null);
            if (target != null) {
                target.setKinderNo(null);
                target.setModifyDate(LocalDateTime.now());

                memberRepository.save(target);
                memberDto = memberRepository.findDtoById(id);
            }
        }
        return memberDto;
    }
}
