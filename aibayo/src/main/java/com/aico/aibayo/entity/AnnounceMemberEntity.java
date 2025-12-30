package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "announce_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(AnnounceMemberId.class)
public class AnnounceMemberEntity {
    @Id
    @Column(name="announce_no")
    private Long announceNo;//공지 번호
    @Id
    @Column(name="id")
    private Long id; //아이디
    @Column(name="is_read")
    private String isRead; //읽음상태 0:안읽음 1:읽음
}
