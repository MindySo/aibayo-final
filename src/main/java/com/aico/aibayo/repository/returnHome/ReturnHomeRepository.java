package com.aico.aibayo.repository.returnHome;

import com.aico.aibayo.entity.ReturnHomeAgreementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReturnHomeRepository extends JpaRepository<ReturnHomeAgreementEntity,Long>,ReturnHomeRepositoryCustom {

}
