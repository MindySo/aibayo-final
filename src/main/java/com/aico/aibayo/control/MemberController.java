package com.aico.aibayo.control;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.MemberStatusEnum;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.entity.MemberEntity;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.repository.member.MemberRepository;
import com.aico.aibayo.service.member.MemberService;
import com.aico.aibayo.service.member.MemberServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;
    private final HttpSession session;
    private final MemberRepository memberRepository;

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
    public String signIn() {
        return "member/signIn";
    }

    @GetMapping("/signUp")
    public String signUp() {
        return "member/signUp";
    }

    @GetMapping("/signUpInviteTeacher")
    public String signUpInviteTeacher() {
        return "/member/signUpInviteTeacher";
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
        System.out.println("signUpKid GetMapping");
        return "member/signUpKid";
    }

    @PostMapping("/signUpKid")
    @ResponseBody
    public String signUpKid(@RequestBody MemberDto member, HttpSession session) {
        session.setAttribute("member", member);
        System.out.println("signUpKid PostMapping");
        return "success";
    }

    @PostMapping("/signUpKinder")
    @ResponseBody
    public String signUpKinder(@RequestBody MemberDto member, HttpSession session) {
        session.setAttribute("member", member);
        // 여기서는 간단하게 모델에 추가하겠습니다.
        return "success";
    }

    @GetMapping("/signUpTeacher")
    public String signUpTeacher(HttpSession session, Model model) {
        System.out.println("signUpTeacher GetMapping");
        MemberDto member = (MemberDto) session.getAttribute("member");
        model.addAttribute("member", member);
        return "/member/signUpTeacher";
    }

    @PostMapping("/signUpTeacher")
    @ResponseBody
    public String signUpTeacher(@RequestBody MemberDto member, HttpSession session) {
        System.out.println("signUpTeacher PostMapping");
        session.setAttribute("member", member);
        return "seccess";

    }


    @PostMapping("/finalSignUp")
    public String finalSignUp(@RequestBody MemberDto member) {
        if (member == null) {
            log.error("MemberDto is null");
            return "/member/signIn"; // 적절한 에러 페이지로 리다이렉트
        }

        // 회원가입 처리 로직 (예: 데이터베이스에 저장)
        log.info("Username: {}", member.getUsername());
        log.info("Name: {}", member.getName());
        log.info("Password: {}", member.getPassword());
        log.info("Phone: {}", member.getPhone());
        log.info("Role: {}", member.getRole());
        log.info("KidName: {}", member.getKidName());
        log.info("Birth: {}", member.getKidBirth());
        log.info("Gender: {}", member.getKidGender());
        log.info("KinderNo: {}", member.getKinderNo());
        log.info("ClassNo: {}", member.getClassNo());
        log.info("Relationship: {}", member.getRelationship());

        MemberDto memberDto = new MemberDto();
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
        memberDto.setStatus(MemberStatusEnum.INACTIVE.getStatus()); // 승인 해줘야 로그인 가능
        memberDto.setRegDate(LocalDateTime.now());
        memberDto.setLatestLogDate(LocalDateTime.now());
        memberDto.setIsMainParent(BooleanEnum.FALSE.getBool());
        memberDto.setInvite(member.getInvite());

        memberService.signUpProcess(memberDto);

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
    public String myPageUpdate(@ModelAttribute MemberDto memberDto) {
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
            return "user/main/main";
        } else {
            return "admin/main/main";
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

        return "/member/signIn";
    }


    @PostMapping("/passwordExist")
    public ResponseEntity<Map<String, Boolean>> passwordExist(@RequestBody Map<String, String> request, @ModelAttribute("loginInfo") MemberDto memberDto) {
        String username = memberDto.getUsername(); // 토큰에서 가져온 username
        String password = request.get("password");

        log.info("passwordExist / username : {}", username);
        log.info("passwordExist / password : {}", password);

        boolean passwordExists = memberService.checkPassword(username, password);

        if (passwordExists) {
            System.out.println("passwordExists true");
        } else {
            System.out.println("passwordExists false");
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", passwordExists);


        return ResponseEntity.ok(response);
    }

    @PostMapping("/updatePassword")
    public String updatepassword(@RequestParam("newPassword") String newPassword, @ModelAttribute("loginInfo") MemberDto memberDto) {
        String username = memberDto.getUsername();

        log.info("updatePassword / username : {}", username);
        log.info("updatePassword / newPassword : {}", newPassword);

        memberService.updatePassword(username, newPassword);

        if (memberDto.getRole().equals("ROLE_ADMIN")) {
            return "/admin/main/main";
        } else {
            return "/user/main/main";
        }
    }

}
