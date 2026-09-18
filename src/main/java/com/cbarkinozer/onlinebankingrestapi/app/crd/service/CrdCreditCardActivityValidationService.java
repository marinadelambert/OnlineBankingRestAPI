package com.cbarkinozer.onlinebankingrestapi.app.crd.service;

import com.cbarkinozer.onlinebankingrestapi.app.crd.enums.CrdErrorMessage;
import com.cbarkinozer.onlinebankingrestapi.app.crd.service.entityservice.CrdCreditCardEntityService;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.IllegalFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class CrdCreditCardActivityValidationService {

    private final CrdCreditCardEntityService crdCreditCardEntityService;

    public void controlIsParameterMinLargerThanMax(BigDecimal min, BigDecimal max) {

        if(min.compareTo(max) > 0){
            throw new IllegalFieldException(CrdErrorMessage.PARAMETER_MIN_CANNOT_BE_LARGER_THAN_MAX);
        }
    }

    public void controlIsCreditCardOfCurrentCustomer(Long creditCardId) {

        crdCreditCardEntityService.getByIdWithControl(creditCardId);
    }
}
