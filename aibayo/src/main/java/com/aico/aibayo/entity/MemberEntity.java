package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; // email

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String phone;

    @Column(name = "role_no")
    private Integer roleNo;

    private String role;

    @Column
    private Integer status;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "inactivate_date")
    private LocalDateTime inactivateDate;

    @Column(name = "latest_log_date")
    private LocalDateTime latestLogDate;

    @Lob
    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "kinder_no") //register_kinder 테이블에서 참고하는 컬럼
    private Long kinderNo;
}