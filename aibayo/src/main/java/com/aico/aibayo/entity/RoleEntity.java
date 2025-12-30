package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RoleEntity {
    @Id
    @Column(name = "role_no")
    private Long roleNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false)
    private Role roleName;
}
