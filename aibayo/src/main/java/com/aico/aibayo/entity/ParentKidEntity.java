package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parent_kid")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(ParentKidId.class)
public class ParentKidEntity {
    @Id
    @Column(name = "id") // member_id
    private Long id;
    @Id
    @Column(name = "kid_no")
    private Long kidNo;
    @Id
    @Column(name = "accept_no")
    private Long acceptNo;
    @Column(name = "is_main_parent")
    private String isMainParent;
    @Column(name = "parent_relationship")
    private String parentRelationship;
}
