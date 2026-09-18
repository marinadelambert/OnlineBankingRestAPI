package com.cbarkinozer.onlinebankingrestapi.app.crd.service.entityservice;

import com.cbarkinozer.onlinebankingrestapi.app.crd.dao.CrdCreditCardDao;
import com.cbarkinozer.onlinebankingrestapi.app.crd.dto.CrdCreditCardDetailsDto;
import com.cbarkinozer.onlinebankingrestapi.app.crd.entity.CrdCreditCard;
import com.cbarkinozer.onlinebankingrestapi.app.crd.enums.CrdErrorMessage;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.ItemNotFoundException;
import com.cbarkinozer.onlinebankingrestapi.app.gen.enums.GenStatusType;
import com.cbarkinozer.onlinebankingrestapi.app.gen.service.BaseEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CrdCreditCardEntityService extends BaseEntityService<CrdCreditCard, CrdCreditCardDao> {

    public CrdCreditCardEntityService(CrdCreditCardDao dao) {
        super(dao);
    }

    public List<CrdCreditCard> findAllByStatusType(GenStatusType statusType){
        return getDao().findAllByStatusType(statusType);
    }

    public List<CrdCreditCard> findAllActiveCreditCardList() {
        return getDao().findAllByStatusType(GenStatusType.ACTIVE);
    }

    public List<Long> findCurrentCustomerCreditCardIdList() {
        Long currentCustomerId = getCurrentCustomerId();

        return getDao().findAllByCusCustomerId(currentCustomerId).stream()
                .map(CrdCreditCard::getId)
                .collect(Collectors.toList());
    }

    public CrdCreditCard getByIdWithOwnershipControl(Long creditCardId) {
        Long currentCustomerId = getCurrentCustomerId();

        return findById(creditCardId)
                .filter(crdCreditCard -> Objects.equals(crdCreditCard.getCusCustomerId(), currentCustomerId))
                .orElseThrow(() -> new ItemNotFoundException(CrdErrorMessage.CREDIT_CARD_NOT_FOUND));
    }

    public CrdCreditCard findByCardNoAndCvvNoAndExpireDate(Long cardNo, Long cvvNo, LocalDate expireDate){
        return getDao().findByCardNoAndCvvNoAndExpireDateAndStatusType(cardNo, cvvNo, expireDate, GenStatusType.ACTIVE);
    }

    public CrdCreditCardDetailsDto getCreditCardDetails(Long creditCardId) {
        return getDao().getCreditCardDetails(creditCardId);
    }
}
