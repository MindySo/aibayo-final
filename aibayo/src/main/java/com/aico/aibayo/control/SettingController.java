package com.aico.aibayo.control;

import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.kinder.KinderDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.RegisterKinderEntity;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.service.kinder.KinderService;
import com.aico.aibayo.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/setting")
@RequiredArgsConstructor
public class SettingController {

    private final KinderService kinderService;




    @GetMapping("/menu")
    public String menu(){
        return "admin/setting/menu";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("loginInfo") MemberDto loginInfo,
            @ModelAttribute("kinderDto") KinderDto kinderDto,
            Model model
    ){
        HashMap<String, Object> memberDto = new HashMap<>();
        memberDto.put("id", loginInfo.getId());

        model.addAttribute("username", loginInfo.getUsername());
        return "admin/setting/add";
    }

    @PostMapping("addOk")
    @ResponseBody
    public ResponseEntity<RegisterKinderDto> addOk (@RequestBody KinderDto kinderDto) {
        log.info("create Kinder: {}", kinderDto);
        RegisterKinderDto inserted = kinderService.insertKinder(kinderDto);
        return ResponseEntity.ok(inserted);
    }


    @GetMapping("/modify/{kinderNo}")
    public String modify(
            @PathVariable Long kinderNo,
            @ModelAttribute("loginInfo") MemberDto loginInfo,
            Model model
    ){
        HashMap<String, Object> memberDto = new HashMap<>();
        memberDto.put("id", loginInfo.getId());

        Optional<RegisterKinderEntity> kinderOptional = kinderService.findByKinderNo(kinderNo);

        // Optional에서 값을 꺼내서 모델에 추가
        if (kinderOptional.isPresent()) {
            RegisterKinderEntity kinderDto = kinderOptional.get();
            model.addAttribute("kinder", kinderDto);
        } else {
            return "redirect:/error";  // 에러 페이지로 리다이렉트 예시
        }

        model.addAttribute("member", memberDto);
        model.addAttribute("kinderModifyDate", LocalDateTime.now());

        log.info("memberDto{}", memberDto);
        log.info("kinderDto{}", kinderOptional);

        return "admin/setting/modify";
    }

    @PutMapping("/modifyOk")
    @ResponseBody
    public void modifyOk(@RequestBody KinderDto kinderDto) {
        log.info("modify kinder: {}", kinderDto);
        kinderService.updateKinder(kinderDto);
    }


    @GetMapping("/info/{kinderNo}")
    public String info(
            @PathVariable Long kinderNo,
            @ModelAttribute("loginInfo") MemberDto loginInfo,
            Model model
    ){
        kinderNo = loginInfo.getKinderNo();
        RegisterKinderDto kinderDto = kinderService.getKinderById(kinderNo);

        model.addAttribute("kinderDto", kinderDto);

        return "admin/setting/info";
    }

    @GetMapping("/test")
    public String test(){
        return "admin/setting/list";
    }
}