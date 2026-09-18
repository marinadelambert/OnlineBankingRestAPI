package com.cbarkinozer.onlinebankingrestapi.app.crd.service;

import com.cbarkinozer.onlinebankingrestapi.app.crd.dto.CrdCreditCardSpendDto;
import com.cbarkinozer.onlinebankingrestapi.app.crd.entity.CrdCreditCard;
import com.cbarkinozer.onlinebankingrestapi.app.crd.entity.CrdCreditCardActivity;
import com.cbarkinozer.onlinebankingrestapi.app.crd.enums.CrdCreditCardActivityType;
import com.cbarkinozer.onlinebankingrestapi.app.crd.enums.CrdErrorMessage;
import com.cbarkinozer.onlinebankingrestapi.app.crd.service.entityservice.CrdCreditCardActivityEntityService;
import com.cbarkinozer.onlinebankingrestapi.app.crd.service.entityservice.CrdCreditCardEntityService;
import com.cbarkinozer.onlinebankingrestapi.app.gen.enums.GenStatusType;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.GenBusinessException;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.GenForbiddenException;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.IllegalFieldException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class CrdCreditCardValidationService {

    private final CrdCreditCardEntityService crdCreditCardEntityService;
    private final CrdCreditCardActivityEntityService crdCreditCardActivityEntityService;

    public void validateCardLimit(BigDecimal currentAvailableLimit) {

        if (currentAvailableLimit.compareTo(BigDecimal.ZERO) < 0){
            throw new GenBusinessException(CrdErrorMessage.INSUFFICIENT_CREDIT_CARD_LIMIT);
        }
    }

    public void validateCreditCard(CrdCreditCard crdCreditCard) {

        if (crdCreditCard == null){
            throw new GenBusinessException(CrdErrorMessage.INVALID_CREDIT_CARD);
        }

        if (crdCreditCard.getExpireDate().isBefore(LocalDate.now())){
            throw new GenBusinessException(CrdErrorMessage.CREDIT_CARD_EXPIRED);
        }
    }


    public void isCutOffDayValid(Integer cutOffDay) {

        if(cutOffDay < 1 || cutOffDay > 31 ){
            throw new IllegalFieldException(CrdErrorMessage.CUT_OFF_DAY_IS_NOT_VALID);
        }
    }

    public void controlIsEarningNotNegative(BigDecimal earning) {

        if(earning.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalFieldException(CrdErrorMessage.EARNING_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlAreFieldsNull(BigDecimal earning, Integer cutOffDay) {

        if(earning == null || cutOffDay == null){
            throw new IllegalFieldException(CrdErrorMessage.FIELDS_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlAreFieldsNull(Long creditCardId, BigDecimal amount) {

        if(creditCardId == null || amount == null){
            throw new IllegalFieldException(CrdErrorMessage.FIELDS_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlAreFieldsNull(CrdCreditCardSpendDto crdCreditCardSpendDto) {

        Long cardNo = crdCreditCardSpendDto.getCardNo();
        Long cvvNo = crdCreditCardSpendDto.getCvvNo();
        LocalDate expireDate = crdCreditCardSpendDto.getExpireDate();
        BigDecimal amount = crdCreditCardSpendDto.getAmount();
        String description = crdCreditCardSpendDto.getDescription();

        boolean hasNull =
                        cardNo == null      ||
                        cvvNo == null       ||
                        expireDate == null  ||
                        amount == null      ||
                        description.isBlank();

        if(hasNull){
            throw new IllegalFieldException(CrdErrorMessage.FIELDS_CANNOT_BE_NEGATIVE);
        }
    }

    public void controlIsCardBelongsToCurrentCustomer(CrdCreditCard crdCreditCard) {

        Long currentCustomerId = crdCreditCardEntityService.getCurrentCustomerId();

        if (crdCreditCard == null || crdCreditCard.getCusCustomerId() == null
                || !crdCreditCard.getCusCustomerId().equals(currentCustomerId)) {
            throw new GenForbiddenException(CrdErrorMessage.CREDIT_CARD_ACCESS_DENIED);
        }
    }

    public void controlIsActivityRefundable(CrdCreditCardActivity crdCreditCardActivity) {

        if (crdCreditCardActivity.getCardActivityType() != CrdCreditCardActivityType.SPEND) {
            throw new IllegalFieldException(CrdErrorMessage.ACTIVITY_NOT_REFUNDABLE);
        }

        if (crdCreditCardActivityEntityService.existsByRefundedActivityId(crdCreditCardActivity.getId())) {
            throw new IllegalFieldException(CrdErrorMessage.ACTIVITY_ALREADY_REFUNDED);
        }
    }

    public void controlIsCardCancelled(GenStatusType genStatusType) {

        if(genStatusType == GenStatusType.PASSIVE){

            throw new IllegalFieldException(CrdErrorMessage.CREDIT_CARD_CANCELLED);
        }
    }
}
