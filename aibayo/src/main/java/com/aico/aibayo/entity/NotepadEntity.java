package com.aico.aibayo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notepad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotepadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notepad_no")
    private Long notepadNo;
    @Column(name = "board_no")
    private Long boardNo;
    @Column
    private Integer weather;
    @Column(name = "has_life_record")
    private String hasLifeRecord;
    @Column(name = "notepad_date")
    private LocalDate notepadDate;
}
