package com.aico.aibayo.repository.meal;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.meal.MealDto;
import com.aico.aibayo.dto.meal.MealSearchCondition;
import com.aico.aibayo.entity.MealDetailEntity;
import com.aico.aibayo.entity.MealEntity;
import com.aico.aibayo.entity.QMealDetailEntity;
import com.aico.aibayo.entity.QMealEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MealRepositoryCustomImpl implements MealRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private final QMealEntity meal = QMealEntity.mealEntity;
    private final QMealDetailEntity mealDetail = QMealDetailEntity.mealDetailEntity;

    @Override
    public MealEntity findByMealNo(Long mealNo) {
        return jpaQueryFactory
                .selectFrom(meal)
                .join(meal.mealDetails, mealDetail)
                .where(
                        meal.mealNo.eq(mealNo),
                        meal.mealDeleteFlag.eq(BooleanEnum.FALSE.getBool()),
                        mealDetail.mealInvisibleFlag.eq(BooleanEnum.FALSE.getBool())
                )
                .orderBy(mealDetail.mealType.asc())
                .fetchJoin()
                .fetchOne();
    }

    @Override
    public List<MealDto> findAllByMealDate(MealSearchCondition condition) {
        return jpaQueryFactory
                .select(Projections.constructor(MealDto.class,
                        meal.mealNo,
                        meal.kinderNo,
                        meal.mealDate,
                        meal.mealRegDate,
                        meal.mealModifyDate,
                        meal.mealDeleteDate
                        ))
                .from(meal)
                .where(
                        meal.mealDate.between(condition.getStartDate(), condition.getEndDate()),
                        meal.kinderNo.eq(condition.getKinderNo()),
                        meal.mealDeleteFlag.eq(BooleanEnum.FALSE.getBool())
                )
                .fetch();
    }
}
