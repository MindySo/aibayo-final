package com.aico.aibayo.control;

import com.aico.aibayo.common.OrderTypeEnum;
import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.announce.AnnounceSearchCondition;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.dto.returnHome.ReturnHomeDto;
import com.aico.aibayo.dto.returnHome.ReturnHomeSearchCondition;
import com.aico.aibayo.repository.OrderFormRepository;
import com.aico.aibayo.repository.returnHome.ReturnHomeRepository;
import com.aico.aibayo.service.returnHome.ReturnHomeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Slf4j
    @Controller
    @RequestMapping("/returnhome")
    @RequiredArgsConstructor
    public class ReturnHomeController {
        private final ReturnHomeService returnHomeService;
        private final OrderFormRepository orderFormRepository;
        private final ReturnHomeRepository returnHomeRepository;

        @GetMapping("/admin/card")
        public String admincard(
                    @ModelAttribute("loginInfo") MemberDto loginInfo,
                    @RequestParam(defaultValue = "1") int page,
                    Model model
        ){
            HashMap<String, Object> hashMap = new HashMap<>();
            ReturnHomeSearchCondition condition = new ReturnHomeSearchCondition();
            condition.setKinderNo(loginInfo.getKinderNo());
            model.addAttribute("KinderNo",loginInfo.getKinderNo());

            hashMap.put("page",page);
            hashMap.put("type","card");
            Page<ReturnHomeDto> homeList= returnHomeService.findAllByKinderNo(condition,hashMap);
            log.info("homeCard(admin){}",homeList);
            model.addAttribute("homeList",homeList);
            return getPageInfoAndGoView(model, homeList, "/admin/home/card");
        }
    private String getPageInfoAndGoView(Model model, Page<ReturnHomeDto> homeList, String view) {
        int totalPages = homeList.getTotalPages();
        int currentPage = homeList.getNumber();
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

        model.addAttribute("homeList", homeList);
        log.info("homeList ------>: {}",homeList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return view;
    }
        @GetMapping("/admin/list")
        public String adminlist(@ModelAttribute("loginInfo") MemberDto loginInfo,
                                @RequestParam(defaultValue = "1")
                                int page,
                                Model model){
            HashMap<String, Object> hashMap = new HashMap<>();
            ReturnHomeSearchCondition condition = new ReturnHomeSearchCondition();
            condition.setKinderNo(loginInfo.getKinderNo());
            model.addAttribute("KinderNo",loginInfo.getKinderNo());

            hashMap.put("page",page);
            hashMap.put("type","list");
            Page<ReturnHomeDto> homeList= returnHomeService.findAllByKinderNo(condition,hashMap);
            log.info("homeList(admin){}",homeList);
            model.addAttribute("homeList",homeList);
            return getPageInfoAndGoView(model, homeList, "/admin/home/list");
        }
        @GetMapping("/admin/detail")
        public String admindetail(){
            return "/admin/home/detail";
        }
        @GetMapping("/user/writeform")
        public String write() {
            return "/user/home/writeForm";
        }
        @GetMapping("/user/detail")
        public String userdetail(){
            return "/user/home/detail";
        }
        @GetMapping("/user/card")
        public String usercard(@RequestParam(defaultValue = "1") int page,
                                @ModelAttribute("loginInfo") MemberDto loginInfo,
                                Model model){
            HashMap<String, Object> hashMap = new HashMap<>();

            ReturnHomeSearchCondition condition = new ReturnHomeSearchCondition();
            condition.setKidNo(loginInfo.getKidNo());
            condition.setKinderNo(loginInfo.getKinderNo());
            condition.setOrderType(OrderTypeEnum.RETURNHOME.getNum());
            model.addAttribute("kinderNo",loginInfo.getKinderNo());
            model.addAttribute("kidNo", loginInfo.getKidNo());
            model.addAttribute("loginInfo", loginInfo);
            hashMap.put("page",page);
            hashMap.put("type","card");
            Page<ReturnHomeDto>homeList=returnHomeService.findAllByKidNo(condition,hashMap);
            log.info("homeCard(user){}",homeList);
            return getPageInfoAndGoViewCard(model, homeList, "/user/home/card");
        }
    private String getPageInfoAndGoViewCard(Model model, Page<ReturnHomeDto> homeList, String view) {
        int totalPages = homeList.getTotalPages();
        int currentPage = homeList.getNumber();
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

        model.addAttribute("homeList", homeList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return view;
    }
        @GetMapping("/user/list")
        public String userlist(
                @RequestParam(defaultValue = "1") int page,
                @ModelAttribute("loginInfo") MemberDto loginInfo,
                Model model
        ){
            HashMap<String, Object> hashMap = new HashMap<>();

            ReturnHomeSearchCondition condition = new ReturnHomeSearchCondition();
            condition.setKidNo(loginInfo.getKidNo());
            condition.setKinderNo(loginInfo.getKinderNo());
            condition.setOrderType(OrderTypeEnum.RETURNHOME.getNum());
            model.addAttribute("kinderNo",loginInfo.getKinderNo());
            model.addAttribute("kidNo", loginInfo.getKidNo());
            model.addAttribute("loginInfo", loginInfo);
            hashMap.put("page",page);
            hashMap.put("type","list");
            Page<ReturnHomeDto>homeList=returnHomeService.findAllByKidNo(condition,hashMap);
            log.info("homeList(user){}",homeList);
            return getPageInfoAndGoViewCard(model, homeList, "/user/home/list");
        }



}
