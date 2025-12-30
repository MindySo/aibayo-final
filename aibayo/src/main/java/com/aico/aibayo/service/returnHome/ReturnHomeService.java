package com.aico.aibayo.service.returnHome;

import com.aico.aibayo.dto.returnHome.ReturnHomeDto;
import com.aico.aibayo.dto.returnHome.ReturnHomeSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface ReturnHomeService {
    Page<ReturnHomeDto> findAllByKinderNo(ReturnHomeSearchCondition condition, HashMap<String, Object> hashMap);
    Page<ReturnHomeDto>findAllByKidNo(ReturnHomeSearchCondition condition, HashMap<String, Object> hashMap);
    ReturnHomeDto findByRhAgreeNo(Long rhAgreeNo);
    void insertReturnHome(ReturnHomeDto returnHomeDto);
    ReturnHomeDto deleteReturnHome(ReturnHomeDto orderNo);

}
