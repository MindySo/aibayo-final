package com.aico.aibayo.control;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.MemberRoleEnum;
import com.aico.aibayo.common.MemberStatusEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.InviteCodeDto;
import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.repository.member.MemberRepository;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.inviteCode.InviteCodeService;
import com.aico.aibayo.service.kid.KidService;
import com.aico.aibayo.service.kinder.KinderService;
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
import jakarta.servlet.http.HttpSession;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final InviteCodeService inviteCodeService;
    private final KidService kidService;
    private final KinderService kinderService;
    private final ClassService classService;


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

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("domain", null);
        return "member/signIn";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "member/signUp";
    }

    @GetMapping({"/signUp/{inviteId}"})
    public String signUpByInviteId(@PathVariable Long inviteId,
                                   Model model) {
        InviteCodeDto inviteCodeDto = inviteCodeService.getByInviteId(inviteId);
        model.addAttribute("inviteId", inviteCodeDto.getInviteId());

        KidDto kidDto = kidService.getByKidNo(inviteCodeDto.getKidNo());
        model.addAttribute("kidInfo", kidDto);

        if (inviteCodeDto == null) {
            model.addAttribute("inviteExpired", true);
            return "redirect:/login";
        }

        return "member/signUpInviteUser";
    }

    @GetMapping("/signUpInviteTeacher")
    public String signUpInviteTeacher() {
        return "member/signUpInviteTeacher";
    }

    @PostMapping("/signUp")
    @ResponseBody
    public String signUpProcess(MemberDto member) {
        return "ok";
    }

    @GetMapping("/signUpKid")
    public String signUpKidForm(HttpSession session, Model model) {
        MemberDto member = (MemberDto) session.getAttribute("member");
        model.addAttribute("member", member);
        List<RegisterKinderDto> kinders = kinderService.getAllKinder();
        model.addAttribute("kinders",kinders);

        System.out.println("signUpKid GetMapping");

        log.info("signUpKid GetMapping");
        return "member/signUpKid";
    }

    @PostMapping("/signUpKid")
    @ResponseBody
    public String signUpKid(@RequestBody MemberDto member, HttpSession session) {
        session.setAttribute("member", member);
        log.info("signUpKid PostMapping");
        return "success";
    }


    @GetMapping("/signUpTeacher")
    public String signUpTeacher(HttpSession session, Model model) {
        log.info("signUpTeacher GetMapping");
        MemberDto member = (MemberDto) session.getAttribute("member");
        model.addAttribute("member", member);
        List<RegisterKinderDto> kinders = kinderService.getAllKinder();
        model.addAttribute("kinders",kinders);
        log.info("signUpKid GetMapping");
        return "member/signUpTeacher";
    }

    @PostMapping("/signUpTeacher")
    @ResponseBody
    public String signUpTeacher(@RequestBody MemberDto member, HttpSession session) {
        log.info("signUpTeacher PostMapping");
        session.setAttribute("member", member);
        return "seccess";
    }

    @PostMapping("/finalSignUp")
    public String finalSignUp(@RequestBody MemberDto member) {
        if (member == null) {
            log.error("MemberDto is null");
            return "member/signIn"; // 적절한 에러 페이지로 리다이렉트
        }
        // 회원가입 처리 로직 (예: 데이터베이스에 저장)
        log.info("finalSignUp MemberDto: {}", member);

        MemberDto memberDto = new MemberDto(); // mapper 람다식으로 dto -> entity
        memberDto.setUsername(member.getUsername());
        memberDto.setName(member.getName());
        memberDto.setPassword(member.getPassword());
        memberDto.setPhone(member.getPhone());
        memberDto.setRole(member.getRole());
        memberDto.setKidNo(member.getKidNo());
        memberDto.setKidName(member.getKidName());
        memberDto.setKidBirth(member.getKidBirth());
        memberDto.setKidGender(member.getKidGender());
        memberDto.setKinderNo(member.getKinderNo());
        memberDto.setClassNo(member.getClassNo());
        memberDto.setRelationship(member.getRelationship());
        memberDto.setRegDate(LocalDateTime.now());
        memberDto.setLatestLogDate(LocalDateTime.now());
        memberDto.setIsMainParent(BooleanEnum.FALSE.getBool());
        memberDto.setInviteId(member.getInviteId());

        if (member.getRole().equals("ROLE_USER")) {
            memberDto.setRoleNo(MemberRoleEnum.PARENT.getRole());
            memberDto.setStatus(MemberStatusEnum.TEMP.getStatus()); // 승인 해줘야 로그인 가능
        } else if (member.getRole().equals("ROLE_TEACHER")){
            memberDto.setRoleNo(MemberRoleEnum.TEACHER.getRole());
            memberDto.setStatus(MemberStatusEnum.TEMP.getStatus()); // 승인 해줘야 로그인 가능
        } else if (member.getRole().equals("ROLE_PRINCIPAL")) {
            memberDto.setRoleNo(MemberRoleEnum.PRINCIPAL.getRole());
            memberDto.setStatus(MemberStatusEnum.ACTIVE.getStatus()); // 승인 해줘야 로그인 가능
        }

        memberService.signUpProcess(memberDto);
        return "redirect:member/signIn";
    }

    @PostMapping("/finalSignUpInvite")
    public String finalSignUpInvite(@RequestBody MemberDto member) {
        Long inviteId = member.getInviteId();
        // inviteId가 유효하지 않은 경우 회원가입 로직을 진행하지 않음
        if (inviteId == null || inviteId <= 0) {
            log.error("Invalid inviteId: {}", inviteId);
            return "member/signIn";
        }
        log.info("Received inviteId: {}", inviteId);

        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(member.getUsername());
        memberDto.setPassword(member.getPassword());
        memberDto.setPhone(member.getPhone());
        memberDto.setName(member.getName());
        memberDto.setRole(member.getRole());
        memberDto.setKidName(member.getKidName());
        memberDto.setKidBirth(member.getKidBirth());
        memberDto.setKidGender(member.getKidGender());
        memberDto.setKinderNo(member.getKinderNo());
        memberDto.setClassNo(member.getClassNo());
        memberDto.setRelationship(member.getRelationship());
        memberDto.setRoleNo(MemberRoleEnum.PARENT.getRole());
        memberDto.setRoleNo(member.getRoleNo());

        // 회원가입 처리 로직
        member.setStatus(MemberStatusEnum.ACTIVE.getStatus());
        member.setRegDate(LocalDateTime.now());
        member.setLatestLogDate(LocalDateTime.now());
        member.setIsMainParent(BooleanEnum.TRUE.getBool());

        memberService.signUpProcess(member);

        // inviteId와 관련된 처리
        log.info("Invite ID: {}", inviteId);
        InviteCodeDto inviteCodeDto = inviteCodeService.getByInviteId(inviteId);
        inviteCodeService.deleteInviteCode(inviteCodeDto);

        return "redirect:/member/signIn";
    }

    @GetMapping("/signInFindPw")
    public String signInFindPw() {
        return "member/signInFindPw";
    }

    @GetMapping("signInResetPw")
    public String singInResetPw() {
        return "member/signInResetPw";
    }


    @GetMapping("/myPage")
    public String myPage(@ModelAttribute("loginInfo") MemberDto memberDto, Model model) {
        String name = memberDto.getName();
        String phone = memberDto.getPhone();
        String username = memberDto.getUsername();

        model.addAttribute("name", name);
        model.addAttribute("phone", phone);
        model.addAttribute("username", username);

        return "member/myPage";
    }

    @PostMapping("/myPageUpdate")
    public String myPageUpdate(@ModelAttribute MemberDto memberDto, Model model) {
        String name = memberDto.getName();
        String phone = memberDto.getPhone();
        String username = memberDto.getUsername();
        String role = memberDto.getRole();

        MemberEntity memberEntity = memberRepository.findByUsername(username).orElse(null);
        if (memberEntity != null) {
            memberEntity.setName(name);
            memberEntity.setPhone(phone);
            memberRepository.save(memberEntity);
        }

        if (role.equals("ROLE_USER")) {
            return "redirect:/main/user";
        } else {
            return "redirect:/main/admin";
        }
    }

    @PostMapping("/deleteMember")
    public String deleteMember(@ModelAttribute("loginInfo") MemberDto memberDto) {
        String username = memberDto.getUsername();
        String role = memberDto.getRole();

        if (role.equals("ROLE_PRINCIPAL") && role.equals("ROLE_ADMIN")) {
            return null; // 선생, 학부모만 계정 삭제 가능
        }

        memberService.deleteMember(username, role);

        return "member/signIn";
    }


    @PostMapping("/passwordExist")
    public ResponseEntity<Map<String, Boolean>> passwordExist(@RequestBody Map<String, String> request, @ModelAttribute("loginInfo") MemberDto memberDto) {
        String username = memberDto.getUsername(); // 토큰에서 가져온 username
        String password = request.get("password");
        log.info("passwordExist : username : {} / password : {}", username, password);

        boolean passwordExists = memberService.checkPassword(username, password);
        if (passwordExists) {
            log.error("passwordExists true");
        } else {
            log.error("passwordExists false");
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", passwordExists);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/updatePassword")
    public String updatepassword(@RequestParam("newPassword") String newPassword, @ModelAttribute("loginInfo") MemberDto memberDto) {
        String username = memberDto.getUsername();
        log.info("updatePassword : username : {} / newPassword : {}", username, newPassword);

        memberService.updatePassword(username, newPassword);

        if (memberDto.getRole().equals("ROLE_ADMIN")) {
            return "admin/main/main";
        } else {
            return "user/main/main";
        }
    }

    // 이메일 유효성 검사
    @PostMapping("/validateEmail")
    public ResponseEntity<?> validateEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isRegistered = memberService.isEmailRegistered(email);

        if (isRegistered) {
            log.info("해당 이메일이 존재합니다: {}", email);
            return ResponseEntity.ok().body(Map.of("success", true, "username", email));
        } else {
            log.warn("해당 이메일이 존재하지 않습니다: {}", email);
            return ResponseEntity.ok().body(Map.of("success", false, "message", "해당 이메일로 가입된 계정이 없습니다."));
        }
    }

    @PostMapping("/sendPasswordResetLink")
    @ResponseBody
    public Map<String, String> sendPasswordResetLink(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String resetLink = "http://ec2-3-36-124-157.ap-northeast-2.compute.amazonaws.com:8080/member/resetPassword?email=" + email;

        boolean emailSent = memberService.sendPasswordResetLink(email, resetLink);

        if (emailSent) {
            return Map.of("status", "success");
        } else {
            return Map.of("status", "failure", "message", "메일 전송에 실패했습니다.");
        }
    }

    @GetMapping("/resetPassword")
    public String getResetPassword(@RequestParam("email") String email, Model model) {
        // 이메일을 모델에 추가하여 Thymeleaf에서 사용 가능하도록 함
        model.addAttribute("email", email);
        return "member/signInResetPw";
    }

    // 비밀번호 재설정
    @PostMapping("/resetPassword")
    public ResponseEntity<?> postResetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        // 비밀번호 재설정
        boolean resetSuccess = memberService.updatePasswordByEmail(email, newPassword);
        if (resetSuccess) {
            log.info("비밀번호 재설정 성공: {}", email);
            return ResponseEntity.ok().body(Map.of("success", true));
        } else {
            log.warn("비밀번호 재설정 실패: {}", email);
            return ResponseEntity.ok().body(Map.of("success", false, "message", "비밀번호 변경에 실패했습니다."));
        }
    }

    @PostMapping("/adminUpdateKinderNo")
    public ResponseEntity<String> updateKinderNo(
            @RequestParam("username") String username,
            @RequestParam("kinderNo") String kinderNo,
            @ModelAttribute("loginInfo") MemberDto memberDto) {

        boolean isUpdated = memberService.adminUpdateKinderNo(username, kinderNo);

        memberDto.setKinderNo(Long.valueOf(kinderNo));
        if (isUpdated) {
            return ResponseEntity.ok("KinderNo updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update KinderNo");
        }
    }
}