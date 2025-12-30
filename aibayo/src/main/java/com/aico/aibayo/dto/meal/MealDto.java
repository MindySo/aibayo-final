package com.aico.aibayo.dto.meal;

import com.aico.aibayo.entity.MealEntity;
import java.util.Comparator;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MealDto {
    private Long mealNo;
    private Long kinderNo;
    private LocalDate mealDate;
    private LocalDateTime mealRegDate;
    private LocalDateTime mealModifyDate;
    private LocalDateTime mealDeleteDate;

    private List<MealDetailDto> mealDetails = new ArrayList<>();

    private boolean isDetail;

    public MealDto(Long mealNo, Long kinderNo, LocalDate mealDate, LocalDateTime mealRegDate, LocalDateTime mealModifyDate, LocalDateTime mealDeleteDate) {
        this.mealNo = mealNo;
        this.kinderNo = kinderNo;
        this.mealDate = mealDate;
        this.mealRegDate = mealRegDate;
        this.mealModifyDate = mealModifyDate;
        this.mealDeleteDate = mealDeleteDate;
    }

    public static MealDto toDto(MealEntity entity) {
        if (entity == null) {
            return null;
        }

        MealDto mealDto = new MealDto(
                entity.getMealNo(),
                entity.getKinderNo(),
                entity.getMealDate(),
                entity.getMealRegDate(),
                entity.getMealModifyDate(),
                entity.getMealDeleteDate()
        );

        // MealDetail이 있는 경우, 리스트를 추가
        if (entity.getMealDetails() != null && !entity.getMealDetails().isEmpty()) {
            List<MealDetailDto> mealDetailDtos = entity.getMealDetails().stream()
                    .map(MealDetailDto::toDto)  // MealDetailEntity -> MealDetailDto 변환
                    .sorted(Comparator.comparing(MealDetailDto::getMealType)) // mealType 오름차순 정렬
                    .collect(Collectors.toList());
            mealDto.setMealDetails(mealDetailDtos);
            mealDto.setDetail(true);
        } else {
            mealDto.setDetail(false);
        }

        return mealDto;
    }

}
