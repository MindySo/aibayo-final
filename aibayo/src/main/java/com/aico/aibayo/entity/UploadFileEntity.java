package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upload_file")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UploadFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_no")
    private Long uploadFileNo;
    @Column(name = "board_no")
    private Long boardNo;
    @Column(name = "approval_no")
    private Long approvalNo;
    @Column(name = "upload_file_origin_name")
    private String uploadFileOriginName;
    @Column(name = "upload_file_save_name")
    private String uploadFileSaveName;
    @Column(name = "upload_file_delete_flag")
    private String uploadFileDeleteFlag;
}
