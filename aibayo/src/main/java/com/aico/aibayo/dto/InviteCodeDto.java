package com.aico.aibayo.dto;

import com.aico.aibayo.entity.InviteCodeEntity;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InviteCodeDto {
    private Long inviteId;
    private Long acceptNo;
    private String verifyCode;
    private Integer inviteType;
    private String inviteEmail;
    private LocalDate inviteRegDate;
    private LocalDate inviteExpireDate;
    private String inviteExpireFlag;
    private Long kinderNo;
    private Long kidNo;

    private String inviteName;
    private String kinderName;

    private String url;

    public InviteCodeDto(Long inviteId, Long acceptNo, String verifyCode, Integer inviteType, String inviteEmail,
                         LocalDate inviteRegDate, LocalDate inviteExpireDate, String inviteExpireFlag, Long kinderNo,
                         Long kidNo, String inviteName, String kinderName) {
        this.inviteId = inviteId;
        this.acceptNo = acceptNo;
        this.verifyCode = verifyCode;
        this.inviteType = inviteType;
        this.inviteEmail = inviteEmail;
        this.inviteRegDate = inviteRegDate;
        this.inviteExpireDate = inviteExpireDate;
        this.inviteExpireFlag = inviteExpireFlag;
        this.kinderNo = kinderNo;
        this.kidNo = kidNo;
        this.inviteName = inviteName;
        this.kinderName = kinderName;
    }

    public InviteCodeDto(Long inviteId, Long acceptNo, String verifyCode, Integer inviteType, String inviteEmail,
                         LocalDate inviteRegDate, LocalDate inviteExpireDate, String inviteExpireFlag, Long kinderNo,
                         Long kidNo) {
        this.inviteId = inviteId;
        this.acceptNo = acceptNo;
        this.verifyCode = verifyCode;
        this.inviteType = inviteType;
        this.inviteEmail = inviteEmail;
        this.inviteRegDate = inviteRegDate;
        this.inviteExpireDate = inviteExpireDate;
        this.inviteExpireFlag = inviteExpireFlag;
        this.kinderNo = kinderNo;
        this.kidNo = kidNo;
    }

    public static InviteCodeDto toDto(InviteCodeEntity entity) {
        if (entity == null) { return null; }

        return new InviteCodeDto(
                entity.getInviteId(),
                entity.getAcceptNo(),
                entity.getVerifyCode(),
                entity.getInviteType(),
                entity.getInviteEmail(),
                entity.getInviteRegDate(),
                entity.getInviteExpireDate(),
                entity.getInviteExpireFlag(),
                entity.getKinderNo(),
                entity.getKidNo()
        );
    }
}
