package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notepad_receiver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotepadReceiverEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notepad_no")
    private Long notepadNo;
    @Column(name = "kid_no")
    private Long kidNo;
    @Column(name = "class_no")
    private Long classNo;
}
