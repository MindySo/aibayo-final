package com.aico.aibayo.control;

import com.aico.aibayo.common.AnnounceTypeEnum;
import com.aico.aibayo.common.BoardTypeEnum;
import com.aico.aibayo.dto.ClassDto;
    import com.aico.aibayo.dto.announce.AnnounceDto;
import com.aico.aibayo.dto.announce.AnnounceSearchCondition;
import com.aico.aibayo.dto.comment.CommentDto;
import com.aico.aibayo.dto.comment.CommentSearchCondition;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.dto.member.MemberSearchCondition;
import com.aico.aibayo.dto.notepad.NotepadDto;
import com.aico.aibayo.entity.BoardEntity;
import com.aico.aibayo.entity.CommentEntity;
import com.aico.aibayo.jwt.JWTUtil;
import com.aico.aibayo.repository.comment.CommentRepository;
import com.aico.aibayo.service.announce.AnnounceService;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.comment.CommentService;
import com.aico.aibayo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Controller
@RequestMapping("/announce")
@RequiredArgsConstructor
public class AnnounceController {
    private final AnnounceService announceService;
    private final ClassService classService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private JWTUtil jwtUtil;


    @GetMapping("/admin/card")
    public String admincard(
            @ModelAttribute("loginInfo") MemberDto loginInfo,
            @RequestParam(defaultValue = "1") int page, Model model
    ){
        HashMap<String, Object> hashMap = new HashMap<>();
        AnnounceSearchCondition condition = new AnnounceSearchCondition();
        condition.setKinderNo(loginInfo.getKinderNo());
        model.addAttribute("KinderNo",loginInfo.getKinderNo());
        hashMap.put("page",page);
        hashMap.put("type","card");
        Page<AnnounceDto>announces= announceService.findAllByKinderNoCard(condition,hashMap);
        log.info("announces!!!{}",announces);

        return getPageInfoAndGoView(model, announces, "admin/announce/card");
    }
    private String getPageInfoAndGoView(Model model, Page<AnnounceDto> announces, String view) {
        int totalPages = announces.getTotalPages();
        int currentPage = announces.getNumber();
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

        model.addAttribute("announces", announces);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return view;
    }
    private String getPageInfoAndGoView(Model model,
                                        Page<AnnounceDto> announces,Page<AnnounceDto> primaryAnnounces, String view) {
        int totalPages = announces.getTotalPages();
        int currentPage = announces.getNumber();
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

        model.addAttribute("announces", announces);
        model.addAttribute("primaryAnnounces", primaryAnnounces);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return view;
    }
    @GetMapping("/admin/list")
        public String adminList(@RequestParam(defaultValue = "1") int page,
                                @ModelAttribute("loginInfo") MemberDto loginInfo,
                                Model model){
            HashMap<String, Object> hashMap1 = new HashMap<>();
            HashMap<String, Object> hashMap2 = new HashMap<>();
            AnnounceSearchCondition condition1 = new AnnounceSearchCondition();
            AnnounceSearchCondition condition2 = new AnnounceSearchCondition();
            condition1.setKinderNo(loginInfo.getKinderNo());

            condition2.setKinderNo(loginInfo.getKinderNo());
            condition2.setAnnouncePrimary("1");

            model.addAttribute("KinderNo",loginInfo.getKinderNo());
            hashMap1.put("page",page);
            hashMap1.put("type","list");
            hashMap2.put("page",1);
            hashMap2.put("type","listPrimary");

            Page<AnnounceDto>announces= announceService.findAllByKinderNoList(condition1,hashMap1);
            Page<AnnounceDto>primaryAnnounces= announceService.findAllByKinderNoList(condition2,hashMap2);

            return getPageInfoAndGoView(model, announces, primaryAnnounces, "admin/announce/list");
        }

    @GetMapping("/admin/write")
    public String writeForm(Model model ,
                            @ModelAttribute("loginInfo") MemberDto loginInfo){
        // 나중에는 로그인 사용자 MemberDto 정보에서 가져오기
        List<ClassDto> classDtos = new ArrayList<>();

        if (loginInfo.getRoleNo() < 2) { // 사이트 관리자/원장
            classDtos = classService.getByKinderNo(loginInfo.getKinderNo());

        } else if (loginInfo.getRoleNo() == 2) { // 교사
            classDtos = classService.getByMemberId(loginInfo.getId());
        }
        HashMap<String, Object> announceInfo = new HashMap<>();
        announceInfo.put("boardType", BoardTypeEnum.ANNOUNCE.getNum());
        announceInfo.put("writer", loginInfo.getId());
        announceInfo.put("boardKinderNo", loginInfo.getKinderNo());
        model.addAttribute("classDtos", classDtos);
        model.addAttribute("announceInfo",announceInfo);
        log.info("writeAnnounce : {} ", announceInfo);

        return "admin/announce/writeForm";
    }
    @PostMapping("/writeOk")
    @ResponseBody
    public void writeOk(@RequestBody AnnounceDto announceDto) {
        log.info("create Announce: {}", announceDto);

        announceService.insertAnnounce(announceDto);
    }

    @GetMapping("/admin/{announceNo}")
    public String admindetail(@PathVariable Long announceNo,
                              @RequestParam(defaultValue = "1") int page,
                              @ModelAttribute("loginInfo") MemberDto loginInfo,
                              Model model){
        AnnounceDto announceDto = announceService.findByAnnounceNo(announceNo);

        HashMap<String, Object> hashMap = new HashMap<>();
        CommentSearchCondition condition = new CommentSearchCondition();
        condition.setKinderNo(loginInfo.getKinderNo());
        condition.setBoardNo(announceDto.getBoardNo());

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>announceDto>>>>>{}",announceDto);

        model.addAttribute("announce",announceDto);
        model.addAttribute("KinderNo", loginInfo.getKinderNo());
        hashMap.put("page", page);
        hashMap.put("type", "list");


        HashMap<String, Object> commentInfo = new HashMap<>();
        commentInfo.put("boardType", BoardTypeEnum.ANNOUNCE.getNum());
        commentInfo.put("commentWriter", loginInfo.getId());
        commentInfo.put("announceNo",announceNo);
        commentInfo.put("commentRegDate", LocalDateTime.now());
        commentInfo.put("boardNo",announceDto.getBoardNo());
        commentInfo.put("commentDeleteFlag",announceDto.getCommentDeleteFlag());;
        commentInfo.put("invisibleFlag",announceDto.getInvisibleFlag());
        commentInfo.put("commentNo",announceDto.getCommentNo());

        model.addAttribute("commentInfo",commentInfo);
        System.out.println("commentInfo!"+commentInfo);
        log.info("commentInfo(admin)!!!! :",commentInfo);

        Page<CommentDto> comments = commentService.findAllByBoardNo(condition, hashMap);
        long commentCount = commentService.countByBoardNoAndInvisibleFlag(announceDto.getBoardNo(),"0");
        model.addAttribute("commentCount",commentCount);
        return getPageInfoAndGoViewComment(model, comments,"admin/announce/detail");
    }
    @GetMapping("/admin/modify/{announceNo}")
    public String modifyForm(@PathVariable Long announceNo,
                             @ModelAttribute("loginInfo") MemberDto loginInfo,
                             Model model){
        HashMap<String, Object> memberDto = new HashMap<>();
        memberDto.put("roleNo", loginInfo.getRoleNo());
        memberDto.put("id", loginInfo.getId());

        AnnounceDto announceDto = announceService.findByAnnounceNo(announceNo);
        model.addAttribute("member",memberDto);
        model.addAttribute("announce",announceDto);
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();
        // 모델에 추가
        model.addAttribute("boardModifyDate", now);

        return "admin/announce/modifyForm";
    }
    @PutMapping("/modifyOk")
    @ResponseBody
    public void modify(@RequestBody AnnounceDto announceDto) {
        log.info("modify announce: {}", announceDto);
        announceService.updateAnnounce(announceDto);
    }
    @DeleteMapping("/delete")
    @ResponseBody
    public AnnounceDto delete(@RequestBody AnnounceDto announceDto) {
        log.info("delete: {}", announceDto);
        AnnounceDto result = announceService.deleteAnnounce(announceDto);
        result.setAn(true);
        return result ;
    }
    //    user
    @GetMapping("/user/card")
    public String usercard(@RequestParam(defaultValue = "1") int page,
                           @ModelAttribute("loginInfo") MemberDto loginInfo,
                           Model model){
        HashMap<String, Object> hashMap = new HashMap<>();
        AnnounceSearchCondition condition = new AnnounceSearchCondition();
        condition.setKinderNo(loginInfo.getKinderNo());
        condition.setAnnounceType(AnnounceTypeEnum.TEACHER.getNum());
        model.addAttribute("KinderNo",loginInfo.getKinderNo());
        hashMap.put("page",page);
        hashMap.put("type","card");
        Page<AnnounceDto>announces= announceService.findAllByKinderNoCard(condition,hashMap);

        return getPageInfoAndGoView(model, announces, "user/announce/card");
    }
    @GetMapping("/user/list")
    public String userList(@RequestParam(defaultValue = "1") int page,
                           @ModelAttribute("loginInfo") MemberDto loginInfo,
                           Model model){
        MemberSearchCondition memberSearchCondition = new MemberSearchCondition();
        memberSearchCondition.setId(14L);
        memberSearchCondition.setKidNo(1L);
//        MemberDto loginInfo=  memberService.getByIdAndKidNo(memberSearchCondition);
        HashMap<String, Object> hashMap1 = new HashMap<>();
        HashMap<String, Object> hashMap2 = new HashMap<>();
        AnnounceSearchCondition condition1 = new AnnounceSearchCondition();
        AnnounceSearchCondition condition2 = new AnnounceSearchCondition();
        condition1.setKinderNo(loginInfo.getKinderNo());
        condition1.setAnnounceType(AnnounceTypeEnum.TEACHER.getNum());


        condition2.setKinderNo(loginInfo.getKinderNo());
        condition2.setAnnouncePrimary("1");
        condition2.setAnnounceType(AnnounceTypeEnum.TEACHER.getNum());

        model.addAttribute("KinderNo",loginInfo.getKinderNo());
        hashMap1.put("page",page);
        hashMap1.put("type","list");
        hashMap2.put("page",1);
        hashMap2.put("type","listPrimary");

        Page<AnnounceDto>announces= announceService.findAllByKinderNoList(condition1,hashMap1);
        Page<AnnounceDto>primaryAnnounces= announceService.findAllByKinderNoList(condition2,hashMap2);

        //댓글 카운트 담기
        AnnounceDto announceDto = new AnnounceDto();
        long commentCount = commentService.countByBoardNoAndInvisibleFlag(announceDto.getBoardNo(),"0");
        model.addAttribute("commentCount",commentCount);
        System.out.println("BoardNo: " + announceDto.getBoardNo());
        condition1.setBoardNo(announceDto.getBoardNo());
        condition2.setBoardNo(announceDto.getBoardNo());

        return getPageInfoAndGoView(model, announces, primaryAnnounces, "user/announce/list");
    }

    private String getPageInfoAndGoViewComment(Model model, Page<CommentDto> comments, String view) {
        int totalPages = comments.getTotalPages();
        int currentPage = comments.getNumber();
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        // 조회된 결과가 없을 때 endPage를 0으로 설정
        if (totalPages == 0) {
            endPage = 0;
        } else { // 페이지 5개 범위 확인
            if (endPage - startPage < 4) {
                if (startPage == 0) {
                    endPage = Math.min(4, totalPages - 1);
                } else if (endPage == totalPages - 1) {
                    startPage = Math.max(0, totalPages - 5);
                }
            }
        }

        log.info(">>>>>>>>>>>>>>>>>>>>>>댓글 pagination");
        log.info("startPage: {}", startPage);
        log.info("endPage: {}", endPage);
        log.info("totalPages: {}", totalPages);
        log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        model.addAttribute("comments", comments);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return view;
    }

    @GetMapping("/user/{announceNo}")
    public String userDetail(@PathVariable Long announceNo,
                             @RequestParam(defaultValue = "1") int page,
                             @ModelAttribute("loginInfo") MemberDto loginInfo,
                             Model model){
        AnnounceDto announceDto = announceService.findByAnnounceNo(announceNo);

        HashMap<String, Object> hashMap = new HashMap<>();
        CommentSearchCondition condition = new CommentSearchCondition();
        condition.setKinderNo(loginInfo.getKinderNo());
        condition.setBoardNo(announceDto.getBoardNo());

        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>announceDto>>>>>{}",announceDto);

        model.addAttribute("announce",announceDto);
        model.addAttribute("KinderNo", loginInfo.getKinderNo());
        hashMap.put("page", page);
        hashMap.put("type", "list");


        HashMap<String, Object> commentInfo = new HashMap<>();
        commentInfo.put("boardType", BoardTypeEnum.ANNOUNCE.getNum());
        commentInfo.put("commentWriter", loginInfo.getId());
        commentInfo.put("announceNo",announceNo);
        commentInfo.put("commentRegDate", LocalDateTime.now());
        commentInfo.put("boardNo",announceDto.getBoardNo());
        commentInfo.put("commentDeleteFlag",announceDto.getCommentDeleteFlag());;
        commentInfo.put("invisibleFlag",announceDto.getInvisibleFlag());
        commentInfo.put("commentNo",announceDto.getCommentNo());

        model.addAttribute("commentInfo",commentInfo);
        System.out.println("commentInfo!"+commentInfo);
        log.info("(user)!!!! :",commentInfo);

        Page<CommentDto> comments = commentService.findAllByBoardNo(condition, hashMap);
        long commentCount = commentService.countByBoardNoAndInvisibleFlag(announceDto.getBoardNo(),"0");
        model.addAttribute("commentCount",commentCount);
        return getPageInfoAndGoViewComment(model, comments,"user/announce/detail");
    }


    @PostMapping("/comment/writeOk")
    @ResponseBody
    public CommentDto writeCommentOk(@RequestBody CommentDto commentDto) {
        log.info("Received commentDto: {}", commentDto); // 추가된 로그

        CommentDto commentDto1 = commentService.insertComment(commentDto);
        commentDto1.setComment(true);

        log.info("Inserted commentDto: {}", commentDto1); // 추가된 로그
        return commentDto1;
    }


    @DeleteMapping("/comment/delete")
    @ResponseBody
    public CommentDto deleteComment(@RequestBody CommentDto commentDto) {
        log.info("delete Comment: {}", commentDto);
        CommentDto result = commentService.deleteComment(commentDto);
        result.setComment(true);
        return result;
    }


    @PutMapping("/comment/user/modify/{commentNo}")
    public String commentModifyForm(@PathVariable Long commentNo ,
                                    @ModelAttribute("loginInfo") MemberDto loginInfo,
                                    Model model){
        HashMap<String, Object> memberDto = new HashMap<>();
        memberDto.put("roleNo", loginInfo.getRoleNo());
        memberDto.put("id", loginInfo.getId());

        Optional<CommentEntity> modifiedComment = commentRepository.findById(commentNo);
        model.addAttribute("member",memberDto);
        model.addAttribute("comment",modifiedComment);
        model.addAttribute("commentModifyDate",LocalDateTime.now());

        return "user/announce/detail";
    }
    @PutMapping("/comment/modifyOk")
    @ResponseBody
    public CommentDto commentModify(@RequestBody CommentDto commentDto){
        log.info("modify comment : {}",commentDto);
        return commentService.updateComment(commentDto);
    }
}

