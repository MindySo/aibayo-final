package com.aico.aibayo.service.returnHome;

import com.aico.aibayo.dto.returnHome.ReturnHomeDto;
import com.aico.aibayo.dto.returnHome.ReturnHomeSearchCondition;
import com.aico.aibayo.repository.OrderFormRepository;
import com.aico.aibayo.repository.returnHome.ReturnHomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnHomeServiceImpl implements ReturnHomeService{
    private final OrderFormRepository orderFormRepository;
    private final ReturnHomeRepository returnHomeRepository;
    private static final int PAGE_SIZE_CARD = 6;
    private static final int PAGE_SIZE_LIST = 20;

    @Override
    public Page<ReturnHomeDto> findAllByKinderNo(ReturnHomeSearchCondition condition, HashMap<String, Object> hashMap) {
        int page = (int) hashMap.get("page");
        String type = (String) hashMap.get("type");
        int pagesize=0;
        if(type.equals("card")){
            pagesize=PAGE_SIZE_CARD;
        }else if(type.equals("list")){
            pagesize=PAGE_SIZE_LIST;
        }
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        return returnHomeRepository.findAllByKinderNo(condition,pageable);
    }

    @Override
    public Page<ReturnHomeDto> findAllByKidNo(ReturnHomeSearchCondition condition, HashMap<String, Object> hashMap) {
        int page = (int) hashMap.get("page");
        String type = (String) hashMap.get("type");
        int pagesize=0;
        if(type.equals("card")){
            pagesize=PAGE_SIZE_CARD;
        }else if(type.equals("list")){
            pagesize=PAGE_SIZE_LIST;
        }
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        return returnHomeRepository.findAllByKidNo(condition,pageable);
    }


    @Override
    public ReturnHomeDto findByRhAgreeNo(Long rhAgreeNo) {
        return returnHomeRepository.findByRhAgreeNo(rhAgreeNo);
    }
}
