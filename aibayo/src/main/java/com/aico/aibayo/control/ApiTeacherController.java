package com.aico.aibayo.control;

import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.teacher.TeacherDto;
import com.aico.aibayo.service.teacher.teacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class ApiTeacherController {
    private final teacherService teacherService;

    @PutMapping("/modifyOk")
    public ResponseEntity<MemberDto> modifyOk(@RequestBody Map<String, Number> requestBody) {
        MemberDto updated = teacherService.updateTeacher(requestBody);
        log.info("modify: {}", requestBody);

        return updated == null ? ResponseEntity.badRequest().build() :
                ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteOk")
    public ResponseEntity<MemberDto> deleteOk(@RequestBody Map<String, Object> requestBody) {
        System.out.println("delete requestBody : " + requestBody);
        MemberDto deleted = teacherService.deleteTeacher(requestBody);
        log.info("deleted: {}", deleted);

        return deleted == null ? ResponseEntity.badRequest().build() :
                ResponseEntity.ok(deleted);
    }
}
