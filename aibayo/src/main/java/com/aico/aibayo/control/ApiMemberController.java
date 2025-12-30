package com.aico.aibayo.control;

import com.aico.aibayo.dto.ClassDto;
import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.dto.kid.KidDto;
import com.aico.aibayo.dto.member.MemberDto;
import com.aico.aibayo.entity.RegisterKinderEntity;
import com.aico.aibayo.service.classManage.ClassService;
import com.aico.aibayo.service.kid.KidService;
import com.aico.aibayo.service.kinder.KinderService;
import com.aico.aibayo.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class ApiMemberController {
    private final MemberService memberService;
    private final KinderService kinderService;
    private final ClassService classService;
    private final KidService kidService;

    @PutMapping("/select")
    public ResponseEntity<RegisterKinderDto>kinderDto(
            @RequestBody RegisterKinderDto kinderDto,
            @ModelAttribute("loginInfo") MemberDto memberDto
    ){
        log.info("kinderDto: {}", kinderDto);

        Long kinderNo = kinderDto.getKinderNo();
        RegisterKinderDto result = kinderService.getKinderDtoById(kinderNo);
        memberDto.setKinderNo(kinderNo);
        return result == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(result);
    }

    @PostMapping("/classList")
    public ResponseEntity<List<ClassDto>> classList(
            @RequestBody RegisterKinderDto kinderDto
    ){
        Long kinderNo = kinderDto.getKinderNo();
        List<ClassDto> result = classService.getByKinderNo(kinderNo);

        return result == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(result);
    }

    @PostMapping("/kidList")
    public ResponseEntity<List<KidDto>> kidList(@RequestBody ClassDto classDto) {
        Long classNo = classDto.getClassNo();
        Long kinderNo = classDto.getKinderNo();
        List<KidDto> result = kidService.getByKinderNoAndClassNo(kinderNo, classNo);
        return result == null ? ResponseEntity.badRequest().build(): ResponseEntity.ok(result);
    }



}
