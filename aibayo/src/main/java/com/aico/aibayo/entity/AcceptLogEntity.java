package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "accept_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AcceptLogEntity {
    @Id
    @Column(name = "accept_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acceptNo;

    @Column(name = "accept_type")
    private Integer acceptType;

    @Column(name = "accept_status")
    private Integer acceptStatus;

    @Column(name = "accept_date")
    private LocalDateTime acceptDate;
    @Column(name = "accept_reg_date")
    private LocalDateTime acceptRegDate;
    @Column(name = "accept_modify_date")
    private LocalDateTime acceptModifyDate; ;
    @Column(name = "accept_delete_date")
    private LocalDateTime acceptDeleteDate;
    @Column(name = "accept_delete_flag")
    private String acceptDeleteFlag;

}
