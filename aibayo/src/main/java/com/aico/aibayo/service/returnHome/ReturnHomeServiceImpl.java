package com.aico.aibayo.service.returnHome;

import com.aico.aibayo.common.BooleanEnum;
import com.aico.aibayo.dto.returnHome.ReturnHomeDto;
import com.aico.aibayo.dto.returnHome.ReturnHomeSearchCondition;
import com.aico.aibayo.entity.OrderFormEntity;
import com.aico.aibayo.entity.ReturnHomeAgreementEntity;
import com.aico.aibayo.entity.ReturnHomeAgreementParentEntity;
import com.aico.aibayo.repository.OrderFormRepository;
import com.aico.aibayo.repository.returnHome.ReturnHomeParentRepository;
import com.aico.aibayo.repository.returnHome.ReturnHomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnHomeServiceImpl implements ReturnHomeService{
    private final ReturnHomeRepository returnHomeRepository;
    private final ReturnHomeParentRepository returnHomeParentRepository;
    private final OrderFormRepository orderFormRepository;
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

    @Override
    public void insertReturnHome(ReturnHomeDto returnHomeDto) {
        OrderFormEntity order = OrderFormEntity.builder()
                .orderNo(returnHomeDto.getOrderNo())
                .orderType(returnHomeDto.getOrderType())
                .orderRequester(returnHomeDto.getOrderRequester())
                .orderRequestDate(LocalDateTime.now())
                .runDate(returnHomeDto.getRunDate())
                .kidNo(returnHomeDto.getKidNo())
                .orderChecked(returnHomeDto.getOrderChecked())
                .orderSpecific(returnHomeDto.getOrderSpecific())
                .orderParentSign(returnHomeDto.getOrderParentSign())
                .orderDeleteDate(returnHomeDto.getOrderDeleteDate())
                .orderDeleteFlag(BooleanEnum.FALSE.getBool())
                .build();
        OrderFormEntity savedOrder= orderFormRepository.save(order);

        ReturnHomeAgreementEntity home =
                    ReturnHomeAgreementEntity.builder()
                            .rhAgreeNo(returnHomeDto.getRhAgreeNo())
                            .orderNo(savedOrder.getOrderNo())
                            .rhTime(returnHomeDto.getRhTime())
                            .rhType(returnHomeDto.getRhType())
                            .build();

        ReturnHomeAgreementEntity savedHome = returnHomeRepository.save(home);

        ReturnHomeAgreementParentEntity parent =
                ReturnHomeAgreementParentEntity.builder()
                        .rhAgreeParentNo(returnHomeDto.getRhAgreeParentNo())
                        .rhAgreeNo(savedHome.getRhAgreeNo())
                        .rhMainParentName(returnHomeDto.getRhMainParentName())
                        .rhMainParentTel(returnHomeDto.getRhMainParentTel())
                        .rhMinorParentName(returnHomeDto.getRhMinorParentName())
                        .rhMinorParentTel(returnHomeDto.getRhMinorParentTel())
                        .build();
        ReturnHomeAgreementParentEntity savedParent = returnHomeParentRepository.save(parent);

        ReturnHomeDto.toDto(savedOrder, savedHome, savedParent);
    }


    @Override
    public ReturnHomeDto deleteReturnHome(ReturnHomeDto orderNo) {
        OrderFormEntity orderFormEntity =
                orderFormRepository.findById(orderNo.getOrderNo()).orElse(null);
        assert orderFormEntity  !=null;
        orderFormEntity.setOrderDeleteDate(LocalDateTime.now());
        orderFormEntity.setOrderDeleteFlag(BooleanEnum.TRUE.getBool());

        OrderFormEntity save = orderFormRepository.save(orderFormEntity);

        return ReturnHomeDto.toDto(save);
    }

}
