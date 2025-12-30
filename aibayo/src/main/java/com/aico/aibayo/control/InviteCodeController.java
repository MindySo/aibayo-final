package com.aico.aibayo.control;

import com.aico.aibayo.dto.InviteCodeDto;
import com.aico.aibayo.dto.RegisterKinderDto;
import com.aico.aibayo.service.inviteCode.InviteCodeService;
import com.aico.aibayo.service.registerKinder.RegisterKinderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/inviteCode")
@RequiredArgsConstructor
public class InviteCodeController {
    private final InviteCodeService inviteCodeService;
    private final RegisterKinderService registerKinderService;

    @PostMapping("/mail")
    public ResponseEntity<InviteCodeDto> mail(@RequestBody InviteCodeDto inviteCodeDto) {
        RegisterKinderDto kinderInfo = registerKinderService.getByKinderNo(inviteCodeDto.getKinderNo());
        inviteCodeDto.setKinderName(kinderInfo.getKinderName());
        InviteCodeDto saved = inviteCodeService.sendAndInsertInviteCode(inviteCodeDto);
        log.info("mail saved: {}", saved);

        return saved == null ? ResponseEntity.badRequest().build() :
                               ResponseEntity.ok(saved);
    }

    @PutMapping("/resendMail")
    public ResponseEntity<InviteCodeDto> modifyOk(@RequestBody InviteCodeDto inviteCodeDto) {
        RegisterKinderDto kinderInfo = registerKinderService.getByKinderNo(inviteCodeDto.getKinderNo());
        inviteCodeDto.setKinderName(kinderInfo.getKinderName());
        log.info("target: {}", inviteCodeDto);
        InviteCodeDto modified = inviteCodeService.sendAndUpdateInviteCode(inviteCodeDto);
        log.info("modified: {}", modified);

        return modified == null ? ResponseEntity.badRequest().build()
                                : ResponseEntity.ok(modified);
    }

    @DeleteMapping("/deleteOk")
    public ResponseEntity<InviteCodeDto> deleteOk(@RequestBody InviteCodeDto inviteCodeDto) {
        InviteCodeDto deleted = inviteCodeService.deleteInviteCode(inviteCodeDto);
        log.info("deleted: {}", deleted);

        return deleted == null ? ResponseEntity.badRequest().build() :
                                 ResponseEntity.ok(deleted);
    }
}
