package com.aico.aibayo.control;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.MemberRoleEnum;
import com.aico.aibayo.common.SggInfoEnum;
import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.service.kid.KidService;
import com.aico.aibayo.service.meal.MealService;
import com.aico.aibayo.service.member.MemberService;
import com.aico.aibayo.service.registerKinder.RegisterKinderService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
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
    private final MemberService memberService;
    private final RegisterKinderService registerKinderService;
    private final MealService mealService;
    private final KidService kidService;
    private final HttpSession session;

    @GetMapping("/")
    public String mainPage() {
        return "/index";
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

        return "/admin/main/main";
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

        System.out.println("user role : " + role);

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

        return "/user/main/main";
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

        return "/user/main/main";
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
        // 유치원 정보 세팅
        RegisterKinderDto kinderInfo = registerKinderService.getByKinderNo(loginInfo.getKinderNo());
        model.addAttribute("kinderInfo", kinderInfo);

        String kinderSggCode = kinderInfo.getSggList();
        SggInfoEnum sggInfo = SggInfoEnum.findByKinderSggCode(kinderSggCode);
        model.addAttribute("sggInfo", sggInfo);
    }
}
