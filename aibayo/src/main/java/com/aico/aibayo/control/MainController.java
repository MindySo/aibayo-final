package com.aico.aibayo.control;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.MemberRoleEnum;
import com.aico.aibayo.common.SggInfoEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.attendance.AttendanceDto;
import com.aico.aibayo.dto.attendance.MainAttendanceDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.kid.KidSearchCondition;
import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.dto.notepad.NotepadDto;
import com.aico.aibayo.dto.teacher.TeacherDto;
import com.aico.aibayo.dto.teacher.TeacherSearchCondition;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.service.announce.AnnounceService;
import com.aico.aibayo.service.attendance.AttendanceService;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.kid.KidService;
import com.aico.aibayo.service.meal.MealService;
import com.aico.aibayo.service.member.MemberService;
import com.aico.aibayo.service.notepad.NotepadService;
import com.aico.aibayo.service.registerKinder.RegisterKinderService;
import com.aico.aibayo.service.teacher.teacherService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final JWTUtil jwtUtil;
    private final HttpSession session;
    private final MemberService memberService;
    private final RegisterKinderService registerKinderService;
    private final MealService mealService;
    private final KidService kidService;
    private final teacherService teacherService;
    private final AttendanceService attendanceService;
    private final ClassService classService;
    private final NotepadService notepadService;
    private final AnnounceService announceService;

    @GetMapping("/")
    public String mainPage() {
        return "/index";
    }

    @GetMapping("/siteAdmin")
    public String siteAdmin(){
        return "admin/setting/list";
    }

    @GetMapping("/admin")
    public String adminMain(HttpServletRequest request, HttpServletResponse response,
                            @ModelAttribute("loginInfo") MemberDto loginInfo, Model model) {
        // 쿠키에서 JWT 토큰을 가져옴
        String token = getTokenFromCookies(request.getCookies());
        if (token == null || jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "redirect:/login";
        }

        // JWT 토큰에서 사용자 정보 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        boolean checkAdmin = memberService.checkAdminKinderNo(username);

        if (!checkAdmin) {
            log.info("admin kinderno not exist");
            return "redirect:/setting/add";
        }

        if (!"ROLE_ADMIN".equals(role) && !"ROLE_PRINCIPAL".equals(role) && !"ROLE_TEACHER".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/login";
        }

        // 사용자 정보를 request에 저장 (필요 시 사용)
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        setMeal(loginInfo, model);
        setKinderInfo(loginInfo, model);
        setLoginInfo(loginInfo, model);
        getTeacherAcceptStatus(loginInfo, model);
        getParentAcceptStatus(loginInfo, model);
        getAttendances(loginInfo, model);

        return "admin/main/main";
    }

    @GetMapping("/user")
    public String userMain(HttpServletRequest request, HttpServletResponse response,
                           @ModelAttribute("loginInfo") MemberDto loginInfo, Model model) {
        // 쿠키에서 JWT 토큰을 가져옴
        String token = getTokenFromCookies(request.getCookies());
        if (token == null || jwtUtil.isExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return "redirect:/login";
        }

        // JWT 토큰에서 사용자 정보 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);
        log.info("user role : {}", role);

        if (!"ROLE_USER".equals(role)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "redirect:/login";
        }

        // 사용자 정보를 request에 저장 (필요 시 사용)
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        setMeal(loginInfo, model);
        setKid(loginInfo, model);
        setKinderInfo(loginInfo, model);
        setLoginInfo(loginInfo, model);
        getLatestBoard(loginInfo, model);
        return "user/main/main";
    }

    @PostMapping("/user/changeKid")
    public String changeKid(@ModelAttribute("loginInfo") MemberDto loginInfo,
                            @RequestBody MemberDto memberDto, Model model) {
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setUsername(loginInfo.getUsername());
        condition.setKidNo(memberDto.getKidNo());
        log.info("selectKid condition: {}", condition);
        loginInfo = memberService.getByUsernameWithParentKid(condition);

        session.setAttribute("loginInfo", loginInfo);
        model.addAttribute("loginInfo", loginInfo);

        setMeal(loginInfo, model);
        setKid(loginInfo, model);
        setKinderInfo(loginInfo, model);
        setLoginInfo(loginInfo, model);

        getLatestBoard(loginInfo, model);


        return "user/main/main";
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

    private void getLatestBoard(MemberDto loginInfo, Model model) {
        // 원생에 대한 개인 혹은 반 최신 알림장 하나 표시
        NotepadDto notepad = notepadService.getTop1ByKidNo(loginInfo.getKidNo());
        model.addAttribute("notepad", notepad);
        log.info("notepad: {}", notepad);

        // 원에 대한 최신 공지 2개 표시
        List<AnnounceDto> announces = announceService.findAllByKinderNoMain(loginInfo.getKinderNo());
        model.addAttribute("announces", announces);
        log.info("announces: {}", announces);
    }

    private void getAttendances(MemberDto loginInfo, Model model) {
        // 오늘 날짜에 대한 모든 반의 원생 출결 확인 정보 저장
        List<ClassDto> classDtos = classService.getByKinderNo(loginInfo.getKinderNo());
        log.info("classDtos: {}", classDtos);

        List<MainAttendanceDto> attendancesByClass = new ArrayList<>();

        for (ClassDto clazz : classDtos) {
            List<AttendanceDto> attendanceDtos =
                    attendanceService.getKids(loginInfo.getKinderNo(), clazz.getClassNo(), LocalDate.now());

            MainAttendanceDto mainAttendanceDto = new MainAttendanceDto();
            mainAttendanceDto.setClassNo(clazz.getClassNo());
            mainAttendanceDto.setClassName(clazz.getClassName());
            mainAttendanceDto.setExist(false);

            if (!attendanceDtos.isEmpty()) {
                mainAttendanceDto.setExist(true);

                int totalCount = attendanceDtos.size();
                mainAttendanceDto.setTotalCount(totalCount);

                int presentCount = (int) attendanceDtos.stream()
                        .filter(dto -> dto.getAttendanceStatus().equals("출석"))
                        .count();
                mainAttendanceDto.setPresentCount(presentCount);

                int kidDropCount = (int) attendanceDtos.stream()
                        .filter(dto -> dto.getKidDrop() != null)
                        .count();
                mainAttendanceDto.setKidDropCount(kidDropCount);

                int kidPickupCount = (int) attendanceDtos.stream()
                        .filter(dto -> dto.getKidPickup() != null)
                        .count();
                mainAttendanceDto.setKidPickupCount(kidPickupCount);

                int absentCount = (int) attendanceDtos.stream()
                        .filter(dto -> dto.getAttendanceStatus().equals("결석"))
                        .count();
                mainAttendanceDto.setAbsentCount(absentCount);
            }

            attendancesByClass.add(mainAttendanceDto);

        }
        log.info("attendancesByClass: {}", attendancesByClass);

        model.addAttribute("attendances", attendancesByClass);
    }

    private void getTeacherAcceptStatus(MemberDto loginInfo, Model model) {
        // 선생 승인상태 조회
        TeacherSearchCondition teacherCondition = new TeacherSearchCondition();
        teacherCondition.setKinderNo(loginInfo.getKinderNo());

        teacherCondition.setAcceptStatus(AcceptStatusEnum.WAIT.getStatus());
        List<TeacherDto> waitingTeacherList = teacherService.getTeacherByKinderNo(teacherCondition);
        model.addAttribute("waitingTeachersCount", waitingTeacherList.size());

        teacherCondition.setAcceptStatus(AcceptStatusEnum.INVITE.getStatus());
        List<TeacherDto> invitedTeacherList = teacherService.getInvitedTeacherByKinderNo(teacherCondition);
        model.addAttribute("invitedTeachersCount", invitedTeacherList.size());
    }

    private void getParentAcceptStatus(MemberDto loginInfo, Model model) {
        // 학부모 승인상태 조회
        KidSearchCondition kidCondition = new KidSearchCondition();
        kidCondition.setKinderNo(loginInfo.getKinderNo());

        kidCondition.setAcceptStatus(AcceptStatusEnum.WAIT.getStatus());
        List<KidDto> kidWaitDtos = kidService.getAllWithParentByClassNoAndAcceptStatus(kidCondition);
        model.addAttribute("waitedKidsCount", kidWaitDtos.size());

        kidCondition.setAcceptStatus(null);
        kidCondition.setInviteAcceptStatus(AcceptStatusEnum.ACCEPT.getStatus());
        List<KidDto> kidInviteDtos = kidService.getAllWithInviteByClassNoAndAcceptStatus(kidCondition);
        model.addAttribute("invitedKidsCount", kidInviteDtos.size());
    }

    private void setKid(MemberDto loginInfo, Model model) {
        // 사용자 정보를 통해 현재 바라보는 원생 정보 가져오기
        KidDto nowKid = kidService.getByKidNo(loginInfo.getKidNo());
        log.info("nowKid: {}", nowKid);

        // 사용자 정보를 통해 연관 있는 원생 정보 가져오기
        List<KidDto> kids = kidService.getAllByParent(loginInfo.getId());
        log.info("kids: {}", kids);

        model.addAttribute("nowKid", nowKid);
        model.addAttribute("kids", kids);
    }

    private static void setLoginInfo(MemberDto loginInfo, Model model) {
        String roleName = MemberRoleEnum.findByType(loginInfo.getRoleNo()).getName();
        model.addAttribute("loginInfo", loginInfo);
        model.addAttribute("roleName", roleName);
    }

    private void setMeal(MemberDto loginInfo, Model model) {
        MealSearchCondition condition = new MealSearchCondition();
        condition.setMealDate(LocalDate.now());
        condition.setKinderNo(loginInfo.getKinderNo());
        condition.setMealDeleteFlag(BooleanEnum.FALSE.getBool());

        MealDto mealDto = mealService.getByToday(condition);
        model.addAttribute("meal", mealDto);
        log.info("today meal: {}", mealDto);
    }

    private void setKinderInfo(MemberDto loginInfo, Model model) {
        Long kinderNo = loginInfo.getKinderNo();

        if (kinderNo == null) {
            log.error("로그인 정보에서 kinderNo 값이 null입니다.");
            model.addAttribute("error", "유효한 kinderNo 값이 필요합니다.");
            return;
        }

        try {
            // 유치원 정보 세팅
            RegisterKinderDto kinderInfo = registerKinderService.getByKinderNo(kinderNo);
            model.addAttribute("kinderInfo", kinderInfo);

            String kinderSggCode = kinderInfo.getSggList();
            SggInfoEnum sggInfo = SggInfoEnum.findByKinderSggCode(kinderSggCode);
            model.addAttribute("sggInfo", sggInfo);
        } catch (EntityNotFoundException e) {
            log.error("유치원 정보를 찾을 수 없습니다: " + e.getMessage());
            model.addAttribute("error", "해당 유치원 정보를 찾을 수 없습니다.");
        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 kinderNo 값: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
    }
}