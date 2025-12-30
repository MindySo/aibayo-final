package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "invite_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class InviteCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id")
    private Long inviteId;
    @Column(name="accept_no")
    private Long acceptNo;
    @Column(name = "verify_code")
    private String verifyCode;
    @Column(name = "invite_type")
    private Integer inviteType;
    @Column(name = "invite_email")
    private String inviteEmail;
    @Column(name = "invite_reg_date")
    private LocalDate inviteRegDate;
    @Column(name = "invite_expire_date")
    private LocalDate inviteExpireDate;
    @Column(name = "invite_expire_flag")
    private String inviteExpireFlag;
    @Column(name = "kinder_no")
    private Long kinderNo;
    @Column(name = "kid_no")
    private Long kidNo;
}
