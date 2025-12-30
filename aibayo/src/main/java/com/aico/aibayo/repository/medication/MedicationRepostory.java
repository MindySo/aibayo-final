package com.aico.aibayo.repository.medication;

import com.aico.aibayo.entity.MedicationDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepostory extends JpaRepository<MedicationDetailEntity,Long>,MedicationRepositoryCustom {
}
