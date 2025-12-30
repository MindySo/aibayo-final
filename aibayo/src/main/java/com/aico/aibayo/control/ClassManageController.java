package com.aico.aibayo.control;

import com.aico.aibayo.dto.classKid.ClassKidDto;
import com.aico.aibayo.dto.classTeacher.ClassTeacherDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.ClassEntity;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.repository.classKid.ClassKidRepository;
import com.aico.aibayo.repository.classManage.ClassRepository;
import com.aico.aibayo.repository.classTeacher.ClassTeacherRepository;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.classManage.ClassServiceImpl;
import com.aico.aibayo.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/classManage")
@RequiredArgsConstructor
@Slf4j
public class ClassManageController {
    private final ClassService classService;
    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        String token = getTokenFromCookies(request.getCookies());
        if (token == null || token.isEmpty()) {
            log.error("JWT token is missing");
            // 여기서 예외를 던지거나, 적절히 처리합니다.
            return;
        }
        String username = jwtUtil.getUsername(token);
        log.info("loginUser: {}", username);
        MemberDto memberDto = memberService.findByUsername(username);
        model.addAttribute("loginInfo", memberDto);
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @GetMapping("/main")
    public String mainPage(@ModelAttribute("loginInfo") MemberDto memberDto, Model model) {
        Long kinderNo = memberDto.getKinderNo();
        List<ClassEntity> classList = classService.getClassList(kinderNo);

        model.addAttribute("classList", classList);
        model.addAttribute("kinderNo", kinderNo);

        return "admin/classManage/main";
    }

    @GetMapping("/detail/{classNo}")
    public ResponseEntity<Map<String, Object>> detailPage(@PathVariable Long classNo) {
        List<ClassKidDto> classKid = classService.getClassKid(classNo);
        List<ClassTeacherDto> classTeacher = classService.getClassTeacher(classNo);

        Map<String, Object> response = new HashMap<>();
        response.put("classKid", classKid);
        response.put("classTeacher", classTeacher);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/updateClassName")
    public ResponseEntity<String> updateClassName(@RequestBody Map<String, Object> request) {
        Long classNo = ((Number) request.get("classNo")).longValue();
        String newClassName = (String) request.get("newClassName");
        log.info("updateClassName / classNo : {}, newClassName : {}", classNo, newClassName);
        try {
            classService.updateClassName(classNo, newClassName);
            return ResponseEntity.ok("반 이름이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반 이름 수정을 실패했습니다.");
        }
    }

    @PostMapping("/deleteClass")
    public ResponseEntity<String> deleteClass(@RequestBody Map<String, Object> request) {
        Long classNo = Long.valueOf(request.get("classNo").toString());

        List<ClassKidDto> classKid = classService.getClassKid(classNo);
        List<ClassTeacherDto> classTeacher = classService.getClassTeacher(classNo);

        if ((!classKid.isEmpty()) && (!classTeacher.isEmpty())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반에 소속된 교사 또는 원생이 존재합니다.");
        }
        classService.deleteClass(classNo);
        return ResponseEntity.ok("반이 삭제되었습니다.");

    }

    @PostMapping("/createClass")
    public ResponseEntity<String> createClass(@ModelAttribute("loginInfo") MemberDto memberDto, @RequestBody Map<String, String> request) {
        try {
            String className = request.get("className");
            Long kinderNo = memberDto.getKinderNo();

            if (className == null || className.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("반 이름을 입력해주세요.");
            }
            classService.createClass(className, kinderNo);
            return ResponseEntity.ok("반을 성공적으로 추가했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("반 추가에 실패했습니다.");
        }
    }
}
