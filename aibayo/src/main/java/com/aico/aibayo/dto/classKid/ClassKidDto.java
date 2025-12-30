package com.aico.aibayo.dto.classKid;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString
public class ClassKidDto {
    private Long classNo;
    private Long kidNo;
    private Long acceptNo;

    private String kidName;
    private Integer acceptStatus;

    public ClassKidDto(Long classNo, Long kidNo, Long acceptNo) {
        this.classNo = classNo;
        this.kidNo = kidNo;
        this.acceptNo = acceptNo;
    }
}
