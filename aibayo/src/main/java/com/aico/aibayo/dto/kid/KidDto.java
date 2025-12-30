package com.aico.aibayo.dto.kid;

import com.aico.aibayo.entity.KidEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KidDto {
    private Long kidNo;
    private Long kinderNo;
    private String kidName;
    private LocalDate kidBirth;
    private Integer kidGender;
    private LocalDateTime admissionDate;
    private LocalDateTime modifyDate;
    private LocalDateTime dischargeDate;
    private String dischargeFlag;

    private Long id;
    private String username;
    private String name;

    private Long classNo;

    private Long acceptNo;
    private Long parentKidAcceptNo;
    private Long inviteCodeAcceptNo;
    private Integer acceptStatus;

    private Long inviteId;
    private String inviteEmail;

    private List<Long> classNoList;


    public KidDto(Long kidNo, Long kinderNo, String kidName, LocalDate kidBirth, Integer kidGender,
                  LocalDateTime admissionDate, LocalDateTime modifyDate, LocalDateTime dischargeDate,
                  String dischargeFlag) {
        this.kidNo = kidNo;
        this.kinderNo = kinderNo;
        this.kidName = kidName;
        this.kidBirth = kidBirth;
        this.kidGender = kidGender;
        this.admissionDate = admissionDate;
        this.modifyDate = modifyDate;
        this.dischargeDate = dischargeDate;
        this.dischargeFlag = dischargeFlag;
    }

    public KidDto(Long kidNo, Long kinderNo, String kidName, LocalDate kidBirth, Integer kidGender,
                  LocalDateTime admissionDate, LocalDateTime modifyDate, LocalDateTime dischargeDate,
                  String dischargeFlag, String username, String name, Long id, Long parentKidAcceptNo) {
        this.kidNo = kidNo;
        this.kinderNo = kinderNo;
        this.kidName = kidName;
        this.kidBirth = kidBirth;
        this.kidGender = kidGender;
        this.admissionDate = admissionDate;
        this.modifyDate = modifyDate;
        this.dischargeDate = dischargeDate;
        this.dischargeFlag = dischargeFlag;
        this.username = username;
        this.name = name;
        this.id = id;
        this.parentKidAcceptNo = parentKidAcceptNo;
    }

    public KidDto(Long kidNo, Long kinderNo, String kidName, LocalDate kidBirth, Integer kidGender,
                  LocalDateTime admissionDate, LocalDateTime modifyDate, LocalDateTime dischargeDate,
                  String dischargeFlag, Long inviteId, String inviteEmail,
                  Long parentKidAcceptNo, Long inviteCodeAcceptNo) {
        this.kidNo = kidNo;
        this.kinderNo = kinderNo;
        this.kidName = kidName;
        this.kidBirth = kidBirth;
        this.kidGender = kidGender;
        this.admissionDate = admissionDate;
        this.modifyDate = modifyDate;
        this.dischargeDate = dischargeDate;
        this.dischargeFlag = dischargeFlag;
        this.inviteId = inviteId;
        this.inviteEmail = inviteEmail;
        this.parentKidAcceptNo = parentKidAcceptNo;
        this.inviteCodeAcceptNo = inviteCodeAcceptNo;
    }

    public static KidDto toDto(KidEntity entity) {
        if (entity == null) { return null; }

        return new KidDto(
            entity.getKidNo(),
            entity.getKinderNo(),
            entity.getKidName(),
            entity.getKidBirth(),
            entity.getKidGender(),
            entity.getAdmissionDate(),
            entity.getModifyDate(),
            entity.getDischargeDate(),
            entity.getDischargeFlag()
        );
    }
}
