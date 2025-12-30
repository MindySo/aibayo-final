package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "electronic_approval")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ElectronicApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_no")
    private Long approvalNo;
    @Column
    private String writer;
    @Column(name = "approval_type")
    private int approvalType;
    @Column(name = "approval_status")
    private int approvalStatus;
    @Column(name = "approval_request_reason")
    private String approvalRequestReason;
    @Column(name = "approval_reject_reason")
    private String approvalRejectReason;
    @Column(name = "request_start_date")
    private LocalDate requestStartDate;
    @Column(name = "request_end_date")
    private LocalDate requestEndDate;
    @Column(name = "approval_reg_date")
    private LocalDateTime approvalRegDate;
    @Column(name = "approval_delete_date")
    private LocalDateTime approvalDeleteDate;
    @Column(name = "approval_modify_date")
    private LocalDateTime approvalModifyDate;
}
