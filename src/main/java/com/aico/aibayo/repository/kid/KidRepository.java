package com.aico.aibayo.repository.kid;

import com.aico.aibayo.entity.KidEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KidRepository extends JpaRepository<KidEntity, Long>, KidRepositoryCustom {
    List<KidEntity> findAllByKinderNoAndDischargeFlagEquals(Long kinderNo, String dischargeFlag);
//    Optional<KidEntity> findByKinderNoAndKidNameAndKidBirthAndKidGenderAndDischargeFlag(Long kinderNO, String kidName, LocalDate kidBirth, Integer kidGender, String dischargeFlag);
    Optional<KidEntity> findByKinderNoAndKidNoAndDischargeFlag(Long kinderNo, Long kidNo, String bool);
}
