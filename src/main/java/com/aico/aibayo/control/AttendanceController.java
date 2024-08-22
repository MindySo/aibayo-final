package com.aico.aibayo.control;

import com.aico.aibayo.dto.attendance.AttendanceDto;
import com.aico.aibayo.dto.classKid.ClassKidDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.AttendanceEntity;
import com.aico.aibayo.entity.ClassEntity;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.repository.attendance.AttendanceRepository;
import com.aico.aibayo.service.attendance.AttendanceService;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {
    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final ClassService classService;
    private final AttendanceService attendanceService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @GetMapping("/admin/main")
    public String adminMain() {
        return "admin/attendance/main";
    }

    @GetMapping("/detailToday")
    public String detailToday(@ModelAttribute("loginInfo") MemberDto memberDto, Model model, @RequestParam("date") String date) {

        LocalDate selectedDate = LocalDate.parse(date);

        List<ClassEntity> classList = classService.getClassList(memberDto.getKinderNo());


        model.addAttribute("date", selectedDate);
        model.addAttribute("classList", classList);
        log.info("day : detailToday");
        return "admin/attendance/detailToday";
    }

    @GetMapping("/detailBefore")
    public String detailBefore(@ModelAttribute("loginInfo") MemberDto memberDto, Model model, @RequestParam("date") String date) {
        LocalDate selectedDate = LocalDate.parse(date);

        // KinderNo에 따라 클래스 리스트를 가져옴
        List<ClassEntity> classList = classService.getClassList(memberDto.getKinderNo());

        model.addAttribute("date", selectedDate);
        model.addAttribute("classList", classList);
        log.info("day : detailBefore");
        return "admin/attendance/detailBefore";
    }

    @GetMapping("/detailAfter")
    public String detailAfter(@ModelAttribute("loginInfo") MemberDto memberDto, Model model, @RequestParam("date") String date) {
        LocalDate selectedDate = LocalDate.parse(date);
        LocalDate todayDate = LocalDate.now(); // 현재 날짜 가져오기

        // KinderNo에 따라 클래스 리스트를 가져옴
        List<ClassEntity> classList = classService.getClassList(memberDto.getKinderNo());

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("todayDate", todayDate);
        model.addAttribute("classList", classList);
        log.info("day : detailAfter");
        return "admin/attendance/detailAfter";
    }


    @GetMapping("/write")
    public String detailWrite(Model model) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
        model.addAttribute("date", date);

        return "admin/attendance/detailWrite";
    }

    @GetMapping("/write/{classNo}")
    public String writePage(@PathVariable Long classNo, Model model, @ModelAttribute("loginInfo") MemberDto memberDto) {
        System.out.println("write classNo : " + classNo);
        model.addAttribute("classNo", classNo);
        model.addAttribute("kinderNo", memberDto.getKinderNo());


        List<ClassKidDto> classKid = classService.getClassKid(classNo);
        model.addAttribute("classKid", classKid);


        return "admin/attendance/detailWrite"; // 이 경로는 작성 페이지의 템플릿 경로로 수정해 주세요.
    }

    @PostMapping("/write")
    public ResponseEntity<?> writePage(@RequestBody List<Map<String, Object>> attendanceList) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Map<String, Object> attendance : attendanceList) {
            AttendanceDto attendanceDto = new AttendanceDto();

            // String으로 받은 후 Long으로 변환
            attendanceDto.setKinderNo(Long.valueOf(attendance.get("kinderNo").toString()));
            attendanceDto.setClassNo(Long.valueOf(attendance.get("classNo").toString()));
            attendanceDto.setKidNo(Long.valueOf(attendance.get("kidNo").toString()));

            attendanceDto.setKidName((String) attendance.get("kidName"));

            // kidDrop 처리
            String kidDropStr = (String) attendance.get("kidDrop");
            if (kidDropStr != null && !kidDropStr.trim().isEmpty()) {
                attendanceDto.setKidDrop(LocalDateTime.parse(kidDropStr, formatter));
            }

            // kidPickup 처리
            String kidPickupStr = (String) attendance.get("kidPickup");
            if (kidPickupStr != null && !kidPickupStr.trim().isEmpty()) {
                attendanceDto.setKidPickup(LocalDateTime.parse(kidPickupStr, formatter));
            }

            attendanceDto.setNote((String) attendance.get("note"));
            attendanceDto.setAttendanceStatus((String) attendance.get("attendanceStatus"));

            attendanceService.createAttendance(attendanceDto);

        }
        return ResponseEntity.ok("Success");
    }


    @PostMapping("/getKidAttendance")
    @ResponseBody
    public List<AttendanceDto> getKidAttendance(@ModelAttribute("loginInfo") MemberDto memberDto, @RequestParam("classNo") Long classNo, @RequestParam("date") String date, Model model) {
        Long kinderNo = memberDto.getKinderNo();
        LocalDate selectedDate = LocalDate.parse(date); // String으로 받은 date를 LocalDate로 변환

        List<AttendanceDto> attendanceList = attendanceService.getKids(kinderNo, classNo, selectedDate);

        return attendanceList;
    }

    @PostMapping("/updateAttendance")
    @ResponseBody
    public ResponseEntity<String> updateAttendance(@RequestBody AttendanceDto request,
                                                   @ModelAttribute("loginInfo") MemberDto memberDto) {
        try {
            Long kidNo = request.getKidNo();
            String attendanceStatus = request.getAttendanceStatus();
            LocalDateTime kidDrop = request.getKidDrop();
            LocalDateTime kidPickup = request.getKidPickup();
            String note = request.getNote();
            LocalDate attendanceDate = request.getAttendanceDate();
            Long classNo = request.getClassNo();

            logger.info("Update request received for kidNo: {}, attendanceDate: {}", kidNo, attendanceDate);

            attendanceService.updateAttendance(kidNo, attendanceStatus, kidDrop, kidPickup, note, attendanceDate, classNo, memberDto);

            return ResponseEntity.ok("출석부 수정 성공");
        } catch (Exception e) {
            logger.error("Error updating attendance for kidNo: {}, attendanceDate: {}", request.getKidNo(), request.getAttendanceDate(), e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("출석부 수정 실패");
        }
    }

}
