package com.aico.aibayo.dto.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.aico.aibayo.entity.MemberEntity;
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
public class MemberDto {
    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private Integer roleNo;
    private String role;
    private Integer status;
    private LocalDateTime regDate;
    private LocalDateTime modifyDate;
    private LocalDateTime inactivateDate;
    private LocalDateTime latestLogDate;
    private String profilePicture;
    private Long kinderNo;
    private Long classNo;

    private Long kidNo;

    private Long acceptNo;
    private Integer acceptStatus;

    private String isMainParent;

    private String kidName;
    private LocalDate kidBirth;
    private String kidGender; // 1:남자, 2:여자

    private String relationship;

    private String invite; // 초대코드 유무

    private Long inviteId;

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture, Long kinderNo, Long classNo, Long kidNo,
                     Long acceptNo, Integer acceptStatus, String isMainParent, String kidName, LocalDate kidBirth,
                     String kidGender, String relationship, String invite) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.kinderNo = kinderNo;
        this.classNo = classNo;
        this.kidNo = kidNo;
        this.acceptNo = acceptNo;
        this.acceptStatus = acceptStatus;
        this.isMainParent = isMainParent;
        this.kidName = kidName;
        this.kidBirth = kidBirth;
        this.kidGender = kidGender;
        this.relationship = relationship;
        this.invite = invite;
    }

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture, Long kinderNo) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.kinderNo = kinderNo;
    }

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture, Long kinderNo, Long acceptNo) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.kinderNo = kinderNo;
        this.acceptNo = acceptNo;
    }

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture, Long kinderNo, String isMainParent) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.kinderNo = kinderNo;
        this.isMainParent = isMainParent;
    }


    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture,
                     Long kinderNo, Long acceptNo, String isMainParent) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.kinderNo = kinderNo;
        this.acceptNo = acceptNo;
        this.isMainParent = isMainParent;
    }

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, Long kinderNo) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.kinderNo = kinderNo;
    }

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture, Long kinderNo, Long kidNo, Long acceptNo, Integer acceptStatus,
                     String isMainParent) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.kinderNo = kinderNo;
        this.kidNo = kidNo;
        this.acceptNo = acceptNo;
        this.acceptStatus = acceptStatus;
        this.isMainParent = isMainParent;
    }

    public MemberDto(Long id, String username, String name, String password, String phone, Integer roleNo, String role,
                     Integer status, LocalDateTime regDate, LocalDateTime modifyDate, LocalDateTime inactivateDate,
                     LocalDateTime latestLogDate, String profilePicture, Long acceptNo, Integer acceptStatus) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.roleNo = roleNo;
        this.role = role;
        this.status = status;
        this.regDate = regDate;
        this.modifyDate = modifyDate;
        this.inactivateDate = inactivateDate;
        this.latestLogDate = latestLogDate;
        this.profilePicture = profilePicture;
        this.acceptNo = acceptNo;
        this.acceptStatus = acceptStatus;
    }

    public static MemberDto toDto(MemberEntity entity) {
        return new MemberDto(
                entity.getId(),
                entity.getUsername(),
                entity.getName(),
                entity.getPassword(),
                entity.getPhone(),
                entity.getRoleNo(),
                entity.getRole(),
                entity.getStatus(),
                entity.getRegDate(),
                entity.getModifyDate(),
                entity.getInactivateDate(),
                entity.getLatestLogDate(),
                entity.getProfilePicture(),
                entity.getKinderNo()
        );
    }

}