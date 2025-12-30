package com.aico.aibayo.repository.classKid;

import com.aico.aibayo.common.AcceptTypeEnum;
import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.classKid.ClassKidDto;
import com.aico.aibayo.entity.QAcceptLogEntity;
import com.aico.aibayo.entity.QClassKidEntity;
import com.aico.aibayo.entity.QKidEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ClassKidRepositoryCustomImpl implements ClassKidRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

//    private final QKidEntity kid = QKidEntity.kidEntity;
    private final QAcceptLogEntity acceptLog = QAcceptLogEntity.acceptLogEntity;
    private final QClassKidEntity classKid = QClassKidEntity.classKidEntity;
    private final QKidEntity kid = QKidEntity.kidEntity;


    @Override
    public List<ClassKidDto> findAllByClassNoAndKidNo(Long classNo, Long kidNo) {

        return jpaQueryFactory
                .select(Projections.constructor(ClassKidDto.class,
                        classKid.classNo,
                        classKid.kidNo,
                        classKid.acceptNo
                        ))
                .from(classKid)
                .join(acceptLog).on(acceptLog.acceptNo.eq(classKid.acceptNo)
                        .and(acceptLog.acceptStatus.eq(1)))
                .where(classKid.classNo.eq(classNo)
                        .and(classKid.kidNo.eq(kidNo)))
                .fetch();
    }

    @Override
    public List<ClassKidDto> findAllByClassNo(Long classNo) {
        return jpaQueryFactory
                .select(Projections.constructor(ClassKidDto.class,
                        classKid.classNo,
                        classKid.kidNo,
                        classKid.acceptNo,
                        kid.kidName,
                        acceptLog.acceptStatus
                        ))
                .from(classKid)
                .join(acceptLog).on(acceptLog.acceptNo.eq(classKid.acceptNo)
                        .and(acceptLog.acceptStatus.eq(1))
                        .and(acceptLog.acceptType.eq(AcceptTypeEnum.CLASS_KID.getType())))
                .join(kid).on(kid.kidNo.eq(classKid.kidNo)
                        .and(kid.dischargeFlag.eq(BooleanEnum.FALSE.getBool())))
                .where(classKid.classNo.eq(classNo))
                .fetch();
    }


}
