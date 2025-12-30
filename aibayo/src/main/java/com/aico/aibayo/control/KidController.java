package com.aico.aibayo.control;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.kid.KidSearchCondition;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.kid.KidService;
import java.util.List;

import com.aico.aibayo.service.member.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/kid")
@RequiredArgsConstructor
public class KidController {
    private final KidService kidService;
    private final ClassService classService;
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @GetMapping("/list")
    public String list(@ModelAttribute("loginInfo") MemberDto loginInfo, Model model) {
        KidSearchCondition condition = new KidSearchCondition();
        return getConditionAndGoList(model, loginInfo, condition);
    }

    @PostMapping("/searchByClass")
    public String searchByClass(@RequestBody KidSearchCondition condition,
                                @ModelAttribute("loginInfo") MemberDto loginInfo,
                                Model model) {
        log.info("search: {}", condition);

        return getConditionAndGoList(model, loginInfo, condition);
    }

    @GetMapping("/{kidNo}")
    public String detail(@PathVariable Long kidNo,
                         @ModelAttribute("loginInfo") MemberDto loginInfo,
                         Model model) {
        getConditionAndGoDetail(kidNo, loginInfo, model);

        return "admin/kid/detail";
    }

    @GetMapping("/user/{kidNo}")
    public String userDetail(@PathVariable Long kidNo,
                             @ModelAttribute("loginInfo") MemberDto loginInfo,
                             Model model) {
        // 주보호자 여부 확인
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setId(loginInfo.getId());
        condition.setKidNo(loginInfo.getKidNo());

        model.addAttribute("isMainParent", loginInfo.getIsMainParent());

        getConditionAndGoDetail(kidNo, loginInfo, model);

        return "user/kid/detail";
    }

    @PostMapping("/modifyOk")
    public String modifyOk(@RequestBody KidDto kidDto,
                           @ModelAttribute("loginInfo") MemberDto loginInfo,
                           Model model) {
        int roleNo = loginInfo.getRoleNo();

        kidService.updateClassKid(kidDto);

        getConditionAndGoDetail(kidDto.getKidNo(), loginInfo, model);

        if (roleNo < 2) {
            return "admin/kid/detail";
        } else if (roleNo == 2) {
            return "user/kid/detail";
        } else {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }

    }

    @GetMapping("/write")
    public String writeForm(@ModelAttribute("loginInfo") MemberDto loginInfo,
                            Model model) {
        if (loginInfo == null) {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }

        List<ClassDto> classAllDtos = classService.getByKinderNo(loginInfo.getKinderNo());
        model.addAttribute("allClass", classAllDtos);


        return "admin/kid/writeForm";
    }

    private void getConditionAndGoDetail(@PathVariable Long kidNo,
                                         @ModelAttribute("loginInfo") MemberDto loginInfo,
                                         Model model) {
//        MemberDto loginInfo = (MemberDto) model.getAttribute("loginInfo");

        KidDto kidDto = kidService.getByKidNo(kidNo);
        List<MemberDto> memberDtos = memberService.getAllByKidNo(kidNo);
        List<ClassDto> classDtos = classService.getAllByKidNo(kidNo);
        List<ClassDto> classAllDtos = classService.getByKinderNo(loginInfo.getKinderNo());

        model.addAttribute("kid", kidDto);
        model.addAttribute("members", memberDtos);
        model.addAttribute("classes", classDtos);
        model.addAttribute("allClass", classAllDtos);
    }

    private String getConditionAndGoList(Model model,
                                         @ModelAttribute("loginInfo") MemberDto loginInfo,
                                         KidSearchCondition condition) {

        condition.setKinderNo(loginInfo.getKinderNo());

        condition.setAcceptStatus(AcceptStatusEnum.ACCEPT.getStatus());
        List<KidDto> kidAcceptDtos = kidService.getAllByClassNoAndAcceptStatus(condition);
        model.addAttribute("kidsAccept", kidAcceptDtos);

        condition.setAcceptStatus(AcceptStatusEnum.WAIT.getStatus());
        List<KidDto> kidWaitDtos = kidService.getAllWithParentByClassNoAndAcceptStatus(condition);
        model.addAttribute("kidsWait", kidWaitDtos);

        condition.setAcceptStatus(null);
        condition.setInviteAcceptStatus(AcceptStatusEnum.ACCEPT.getStatus());
        List<KidDto> kidInviteDtos = kidService.getAllWithInviteByClassNoAndAcceptStatus(condition);
        model.addAttribute("kidsInvite", kidInviteDtos);

        List<ClassDto> classDtos = classService.getByKinderNo(loginInfo.getKinderNo());
        model.addAttribute("classes", classDtos);
        model.addAttribute("kinderNo", loginInfo.getKinderNo());

        return "admin/kid/list";
    }
}
