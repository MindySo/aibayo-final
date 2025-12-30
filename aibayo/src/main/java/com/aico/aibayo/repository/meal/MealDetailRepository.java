package com.aico.aibayo.repository.meal;

import com.aico.aibayo.dto.meal.MealDetailDto;
import com.aico.aibayo.entity.MealDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealDetailRepository extends JpaRepository<MealDetailEntity, Long> {
}
