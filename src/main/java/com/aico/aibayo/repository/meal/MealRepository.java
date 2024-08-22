package com.aico.aibayo.repository.meal;

import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.entity.MealEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<MealEntity, Long>, MealRepositoryCustom {
    Optional<MealEntity> findTop1ByMealDateAndKinderNoAndMealDeleteFlag(LocalDate now, Long kinderNo, String bool);
}
