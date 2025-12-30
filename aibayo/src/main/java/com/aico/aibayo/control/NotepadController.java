package com.aico.aibayo.control;

import com.aico.aibayo.common.BoardTypeEnum;
import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.notepad.NotepadDto;
//import com.aico.aibayo.service.notepad.NotepadService;
import com.aico.aibayo.dto.notepad.NotepadSearchCondition;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.kid.KidService;
import com.aico.aibayo.service.notepad.NotepadService;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/notepad")
@RequiredArgsConstructor
public class NotepadController {
    private final NotepadService notepadService;
    private final ClassService classService;
    private final KidService kidService;

    @GetMapping("/admin/list")
    public String adminList(@ModelAttribute("loginInfo") MemberDto loginInfo,
                            @RequestParam(defaultValue = "1") int page, Model model) {
        // 역할에 따라 사용자/관리자 구분하여 이동
        // 사용자의 유치원번호의 사용자가 등록한 모든 알림장 조회
        NotepadSearchCondition condition = new NotepadSearchCondition();
        condition.setKinderNo(loginInfo.getKinderNo());

        model.addAttribute("kinderNo", loginInfo.getKinderNo());

        Page<NotepadDto> notepads = notepadService.getAllByKinderNo(condition, page);

        // 페이지네이션에 필요한 정보
        return getPageInfoAndGoView(model, notepads, "admin/notepad/list");
    }

    @PostMapping("/admin/searchDate")
    public String adminSearchDate(@RequestBody NotepadSearchCondition condition,
                                  Model model) {
        log.info("{}", condition);

        Page<NotepadDto> notepads = notepadService.getAllByKinderNo(condition, 1);

        // 페이지네이션에 필요한 정보
        return getPageInfoAndGoView(model, notepads, "admin/notepad/list");
    }

    @GetMapping("/user/list")
    public String userList(@ModelAttribute("loginInfo") MemberDto loginInfo,
                           @RequestParam(defaultValue = "1") int page, Model model) {
        NotepadSearchCondition condition = new NotepadSearchCondition();
        condition.setKidNo(loginInfo.getKidNo());

        model.addAttribute("kidNo", loginInfo.getKidNo());

        Page<NotepadDto> notepads = notepadService.getAllByKidNo(condition, page);

        return getPageInfoAndGoView(model, notepads, "user/notepad/list");
    }

    @PostMapping("/user/searchDate")
    public String userSearchDate(@RequestBody NotepadSearchCondition condition,
                                 Model model) {
        log.info("{}", condition);

        Page<NotepadDto> notepads = notepadService.getAllByKidNo(condition, 1);

        // 페이지네이션에 필요한 정보
        return getPageInfoAndGoView(model, notepads, "user/notepad/list");
    }

    // 나중에 detail 대신 notepadNo 대신 가져오기
    @GetMapping("/admin/{notepadNo}")
    public String adminDetail(@ModelAttribute("loginInfo") MemberDto loginInfo,
                              @PathVariable Long notepadNo, Model model) {
        // 나중에는 로그인 사용자 MemberDto 정보에서 가져오기
//        HashMap<String, Object> memberDto = new HashMap<>();
//        memberDto.put("roleNo", roleNo);
//        memberDto.put("id", id);

        NotepadDto notepadDto = notepadService.getByNotepadNo(notepadNo);

//        model.addAttribute("member", memberDto);
        model.addAttribute("loginInfo", loginInfo);
        model.addAttribute("notepad", notepadDto);

        return "admin/notepad/detail";
    }

    @GetMapping("/user/{notepadNo}")
    public String userDetail(@PathVariable Long notepadNo, Model model) {
        NotepadDto notepadDto = notepadService.getByNotepadNo(notepadNo);
        model.addAttribute("notepad", notepadDto);
        return "user/notepad/detail";
    }

    @GetMapping("/admin/modify/{notepadNo}")
    public String modifyForm(@PathVariable Long notepadNo, Model model) {
        // 나중에는 로그인 사용자 MemberDto 정보에서 가져오기
//        HashMap<String, Object> memberDto = new HashMap<>();
//        memberDto.put("roleNo", roleNo);
//        memberDto.put("id", id);

        NotepadDto notepadDto = notepadService.getByNotepadNo(notepadNo);

//        model.addAttribute("member", memberDto);
        model.addAttribute("notepad", notepadDto);

        return "admin/notepad/modifyForm";
    }

    @PutMapping("/modifyOk")
    @ResponseBody
    public void modify(@RequestBody NotepadDto notepadDto) {
        log.info("modify: {}", notepadDto);
        notepadService.updateNotepad(notepadDto);
    }

    @GetMapping("/admin/write")
    public String writeForm(@ModelAttribute("loginInfo") MemberDto loginInfo,
                            Model model) {
        // 나중에는 로그인 사용자 MemberDto 정보에서 가져오기
        List<ClassDto> classDtos = new ArrayList<>();
        List<KidDto> kidDtos = new ArrayList<>();

        if (loginInfo.getRoleNo() < 2) { // 사이트 관리자/원장
            classDtos = classService.getByKinderNo(loginInfo.getKinderNo());
            kidDtos = kidService.getByKinderNo(loginInfo.getKinderNo());

        } else if (loginInfo.getRoleNo() == 2) { // 교사
            classDtos = classService.getByMemberId(loginInfo.getId());
            kidDtos = kidService.getByMemberId(loginInfo.getId());
        }

        HashMap<String, Object> notepadInfo = new HashMap<>();
        notepadInfo.put("boardType", BoardTypeEnum.NOTEPAD.getNum());
        notepadInfo.put("writer", loginInfo.getId());
        notepadInfo.put("boardKinderNo", loginInfo.getKinderNo());

        model.addAttribute("classDtos", classDtos);
        model.addAttribute("kidDtos", kidDtos);
        model.addAttribute("notepadInfo", notepadInfo);

        return "admin/notepad/writeForm";
    }

    @PostMapping("/writeOk")
    @ResponseBody
    public void writeOk(@RequestBody NotepadDto notepadDto) {
        log.info("create: {}", notepadDto);
        notepadService.insertNotepad(notepadDto);
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public void delete(@RequestBody NotepadDto notepadDto) {
        log.info("delete: {}", notepadDto);
        notepadService.deleteNotepad(notepadDto);
    }

    private String getPageInfoAndGoView(Model model, Page<NotepadDto> notepads, String view) {
        int totalPages = notepads.getTotalPages();
        int currentPage = notepads.getNumber();
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        // 조회된 결과가 없을 때 endPage를 0으로 설정
        if (totalPages == 0) {
            endPage = 0;
        } else {// 페이지 5개 범위 확인
            if (endPage - startPage < 4) {
                if (startPage == 0) {
                    endPage = Math.min(4, totalPages - 1);
                } else if (endPage == totalPages - 1) {
                    startPage = Math.max(0, totalPages - 5);
                }
            }
        }

        log.info(">>>>>>>>>>>>>>>>>>>>>>pagination");
        log.info("startPage: {}", startPage);
        log.info("endPage: {}", endPage);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        model.addAttribute("notepads", notepads);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return view;
    }
}
