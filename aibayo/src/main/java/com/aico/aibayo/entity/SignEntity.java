package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sign")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class SignEntity {
    @Id
    @Column(name="order_parent_sign")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderParentSign; //보호자 서명
    @Column(name="order_parent_id")
    private String orderParentId; //보호자 아이디
    @Column(name="file_name")
    private String fileName; //파일명
    @Column(name="file_path")
    private String filePath; //파일 경로
}
