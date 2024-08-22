package com.aico.aibayo.service.meal;

import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MealService {
    List<MealDto> getAllByMealDate(MealSearchCondition condition);
    MealDto getByMealNo(Long mealNo);
    MealDto insertMeal(MealDto mealDto, List<MultipartFile> files);
    MealDto updateMeal(MealDto mealDto, List<MultipartFile> files);
    MealDto deleteMeal(MealDto mealDto);
    MealDto getByToday(MealSearchCondition condition);
}
