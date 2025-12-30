package com.aico.aibayo.service.medication;

import com.aico.aibayo.dto.medication.MedicationDto;
import com.aico.aibayo.dto.medication.MedicationSearchCondition;
import org.springframework.data.domain.Page;

import java.util.HashMap;

public class MedicationServiceImpl implements MedicationService{
    @Override
    public Page<MedicationDto> findAllByKinderNoList(MedicationSearchCondition condition, HashMap<String, Object> hashMap) {
        return null;
    }

    @Override
    public Page<MedicationDto> findAllByKinderNoCard(MedicationSearchCondition condition, HashMap<String, Object> hashMap) {
        return null;
    }

    @Override
    public MedicationDto findByAnnounceNo(Long orderNo) {
        return null;
    }
}
