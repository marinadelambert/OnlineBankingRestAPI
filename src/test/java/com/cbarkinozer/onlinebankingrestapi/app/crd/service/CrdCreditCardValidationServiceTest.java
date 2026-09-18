package com.cbarkinozer.onlinebankingrestapi.app.crd.service;

import com.cbarkinozer.onlinebankingrestapi.app.crd.entity.CrdCreditCard;
import com.cbarkinozer.onlinebankingrestapi.app.crd.service.entityservice.CrdCreditCardEntityService;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrdCreditCardValidationServiceTest {

    @Mock
    private CrdCreditCardEntityService crdCreditCardEntityService;

    @InjectMocks
    private CrdCreditCardValidationService crdCreditCardValidationService;

    @Test
    void shouldNotThrow_WhenCreditCardBelongsToCurrentCustomer() {

        CrdCreditCard crdCreditCard = new CrdCreditCard();
        crdCreditCard.setCusCustomerId(1L);

        when(crdCreditCardEntityService.getCurrentCustomerId()).thenReturn(1L);

        assertDoesNotThrow(() ->
                crdCreditCardValidationService.controlIsCreditCardBelongsToCurrentCustomer(crdCreditCard));
    }

    @Test
    void shouldThrow_WhenCreditCardBelongsToAnotherCustomer() {

        CrdCreditCard crdCreditCard = new CrdCreditCard();
        crdCreditCard.setCusCustomerId(2L);

        when(crdCreditCardEntityService.getCurrentCustomerId()).thenReturn(1L);

        assertThrows(UnauthorizedAccessException.class, () ->
                crdCreditCardValidationService.controlIsCreditCardBelongsToCurrentCustomer(crdCreditCard));
    }

    @Test
    void shouldThrow_WhenThereIsNoCurrentCustomer() {

        CrdCreditCard crdCreditCard = new CrdCreditCard();
        crdCreditCard.setCusCustomerId(1L);

        when(crdCreditCardEntityService.getCurrentCustomerId()).thenReturn(null);

        assertThrows(UnauthorizedAccessException.class, () ->
                crdCreditCardValidationService.controlIsCreditCardBelongsToCurrentCustomer(crdCreditCard));
    }
}
