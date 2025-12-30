package com.aico.aibayo.service.inviteCode;

import com.aico.aibayo.common.AcceptStatusEnum;
import com.aico.aibayo.common.AcceptTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.common.InviteTypeEnum;
import com.aico.aibayo.dto.InviteCodeDto;
import com.aico.aibayo.entity.AcceptLogEntity;
import com.aico.aibayo.entity.InviteCodeEntity;
import com.aico.aibayo.repository.AcceptLogRepository;
import com.aico.aibayo.repository.InviteCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteCodeServiceImpl implements InviteCodeService {
    private final InviteCodeRepository inviteCodeRepository;
    private final AcceptLogRepository acceptLogRepository;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Override
    @Transactional
    public InviteCodeDto sendAndInsertInviteCode(InviteCodeDto inviteCodeDto) {
        InviteCodeDto inserted = insertInviteCode(inviteCodeDto);
        inserted.setKinderName(inviteCodeDto.getKinderName());
        inserted.setUrl(inviteCodeDto.getUrl());
        sendEmail(inserted);

        return inserted;
    }

    @Override
    @Transactional
    public InviteCodeDto sendAndUpdateInviteCode(InviteCodeDto inviteCodeDto) {
        acceptLogRepository.findById(inviteCodeDto.getAcceptNo()).ifPresent(target -> {
            target.setAcceptModifyDate(LocalDateTime.now());
            acceptLogRepository.save(target);
        });

        InviteCodeEntity updated =
                inviteCodeRepository.findById(inviteCodeDto.getInviteId())
                        .map(target -> {
                            target.setInviteExpireDate(LocalDate.now().plusDays(7));

                            return inviteCodeRepository.save(target);
                        })
                        .orElse(null);

        InviteCodeDto updatedDto = InviteCodeDto.toDto(updated);
        Objects.requireNonNull(updatedDto).setKinderName(inviteCodeDto.getKinderName());
        Objects.requireNonNull(updatedDto).setUrl(inviteCodeDto.getUrl());
        sendEmail(updatedDto);

        return updatedDto;
    }

    @Override
    @Transactional
    public InviteCodeDto deleteInviteCode(InviteCodeDto inviteCodeDto) {
        acceptLogRepository.findById(inviteCodeDto.getAcceptNo()).ifPresent(target -> {
            target.setAcceptStatus(AcceptStatusEnum.DELETE.getStatus());
            target.setAcceptDeleteDate(LocalDateTime.now());
            target.setAcceptDeleteFlag(BooleanEnum.TRUE.getBool());

            acceptLogRepository.save(target);
        });

        InviteCodeEntity deleted =
                inviteCodeRepository.findById(inviteCodeDto.getInviteId())
                        .map(target -> {
                            target.setInviteExpireDate(LocalDate.now());
                            target.setInviteExpireFlag(BooleanEnum.TRUE.getBool());

                            return inviteCodeRepository.save(target);
                        })
                        .orElse(null);

        return InviteCodeDto.toDto(deleted);
    }

    @Override
    public InviteCodeDto getByInviteId(Long inviteId) {
        InviteCodeEntity target =
                inviteCodeRepository.findByInviteIdAndInviteExpireFlag(inviteId, BooleanEnum.FALSE.getBool())
                .orElse(null);

        return InviteCodeDto.toDto(target);
    }

    private void sendEmail(InviteCodeDto inviteCodeDto) {
        if (inviteCodeDto == null) {
            log.warn("InviteCodeDto is null");
            throw new IllegalArgumentException();
        }

        log.info("sendEmail: {}", inviteCodeDto);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        String desc = InviteTypeEnum.findByType(inviteCodeDto.getInviteType())
                                    .getDesc();
        // 추후 aibayo에 유치원명 들어가도록 변경
        String subject = "[" + inviteCodeDto.getKinderName() + "]초대 메일이 도착했습니다. (" + desc + ")";

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(inviteCodeDto.getInviteEmail()); // 메일 수신자
            mimeMessageHelper.setSubject(subject); // 메일 제목
            mimeMessageHelper.setText(setContext(inviteCodeDto), true);  // 메일 본문, HTML 여부
            mimeMessageHelper.setFrom(new InternetAddress("admin@aico.co.kr", "aibayo"));
            javaMailSender.send(mimeMessage); // 메일 발송
            log.info(" SEND MAIL SUCCESS >>>>>>>>>>>>>>>>>>>>>>>>>> ");
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String setContext(InviteCodeDto inviteCodeDto) {
        Context context = new Context();
        context.setVariable("verifyCode", inviteCodeDto.getVerifyCode());
        context.setVariable("inviteId", inviteCodeDto.getInviteId());
        context.setVariable("inviteEmail", inviteCodeDto.getInviteEmail());
        context.setVariable("kinderName", inviteCodeDto.getKinderName());
        context.setVariable("url", inviteCodeDto.getUrl());
      
        return springTemplateEngine.process("admin/inviteCode/email", context);
    }

    private InviteCodeDto insertInviteCode(InviteCodeDto inviteCodeDto) {
        AcceptLogEntity acceptLogEntity = AcceptLogEntity.builder()
                .acceptType(AcceptTypeEnum.INVITE_CODE.getType())
                .acceptStatus(AcceptStatusEnum.ACCEPT.getStatus())
                .acceptDate(LocalDateTime.now())
                .acceptRegDate(LocalDateTime.now())
                .acceptDeleteFlag(BooleanEnum.FALSE.getBool())
                .build();
        AcceptLogEntity savedAcceptLog =
                acceptLogRepository.save(acceptLogEntity);

        InviteCodeEntity inviteCodeEntity = InviteCodeEntity.builder()
                .acceptNo(savedAcceptLog.getAcceptNo())
                .verifyCode(createCode())
                .inviteType(inviteCodeDto.getInviteType())
                .inviteEmail(inviteCodeDto.getInviteEmail())
                .inviteRegDate(LocalDate.now())
                .inviteExpireDate(LocalDate.now().plusDays(7))
                .inviteExpireFlag(BooleanEnum.FALSE.getBool())
                .kinderNo(inviteCodeDto.getKinderNo())
                .kidNo(inviteCodeDto.getKidNo())
                .build();
        InviteCodeEntity savedInviteCode =
                inviteCodeRepository.save(inviteCodeEntity);

        return InviteCodeDto.toDto(savedInviteCode);
    }

    private String createCode() {
        return UUID.randomUUID().toString();
    }
}
