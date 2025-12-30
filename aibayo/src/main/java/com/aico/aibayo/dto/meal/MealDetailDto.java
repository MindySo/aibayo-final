package com.aico.aibayo.dto.meal;

import com.aico.aibayo.common.MealTypeEnum;
import com.aico.aibayo.entity.MealDetailEntity;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MealDetailDto {
    private Long mealDetailNo;
    private Integer mealType;
    private String mealTypeName;
    private String mealMenu;
    private String mealPic;
    private String mealPicOriginalName;
    private String mealInvisibleFlag;

    public static MealDetailDto toDto(MealDetailEntity entity) {
        if (entity == null) {
            return null;
        }

        return new MealDetailDto(
                entity.getMealDetailNo(),
                entity.getMealType(),
                MealTypeEnum.findByType(entity.getMealType()).getName(),
                entity.getMealMenu(),
                entity.getMealPic(),
                entity.getMealPicOriginalName(),
                entity.getMealInvisibleFlag()
        );
    }
}
