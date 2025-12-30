package com.aico.aibayo.dto;

import com.aico.aibayo.entity.ParentKidEntity;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParentKidDto {
    private Long id;
    private Long kidNo;
    private Long acceptNo;
    private String isMainParent;
    private String parentRelationship;

    public static ParentKidDto toDto(ParentKidEntity entity) {
        if (entity == null) { return null; }

        return new ParentKidDto(
                entity.getId(),
                entity.getKidNo(),
                entity.getAcceptNo(),
                entity.getIsMainParent(),
                entity.getParentRelationship()
        );
    }
}
