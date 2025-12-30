package com.aico.aibayo.repository.meal;

import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import com.aico.aibayo.entity.MealDetailEntity;
import com.aico.aibayo.entity.MealEntity;

import java.util.List;

public interface MealRepositoryCustom {
    MealEntity findByMealNo(Long mealNo);
    List<MealDto> findAllByMealDate(MealSearchCondition condition);
}
