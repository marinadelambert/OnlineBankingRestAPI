package com.cbarkinozer.onlinebankingrestapi.app.gen.service;

import com.cbarkinozer.onlinebankingrestapi.app.acc.dao.AccAccountDao;
import com.cbarkinozer.onlinebankingrestapi.app.acc.entity.AccAccount;
import com.cbarkinozer.onlinebankingrestapi.app.acc.entity.AccAccountActivity;
import com.cbarkinozer.onlinebankingrestapi.app.acc.dao.AccAccountActivityDao;
import com.cbarkinozer.onlinebankingrestapi.app.acc.service.entityservice.AccAccountActivityEntityService;
import com.cbarkinozer.onlinebankingrestapi.app.acc.service.entityservice.AccAccountEntityService;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.ItemNotFoundException;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.UnauthorizedAccessException;
import com.cbarkinozer.onlinebankingrestapi.app.sec.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaseEntityServiceTest {

    private static final Long CURRENT_CUSTOMER_ID = 1L;
    private static final Long OTHER_CUSTOMER_ID = 2L;
    private static final Long ACCOUNT_ID = 10L;

    @Mock
    private AccAccountDao accAccountDao;

    @Mock
    private AccAccountActivityDao accAccountActivityDao;

    @Mock
    private AuthenticationService authenticationService;

    private AccAccountEntityService accAccountEntityService;

    @BeforeEach
    void setUp() {

        accAccountEntityService = new AccAccountEntityService(accAccountDao);
        accAccountEntityService.setAuthenticationService(authenticationService);
    }

    @Test
    void shouldGetByIdWithControl_WhenAccount_BelongsToCurrentCustomer() {

        AccAccount accAccount = new AccAccount();
        accAccount.setCustomerId(CURRENT_CUSTOMER_ID);

        when(accAccountDao.findById(ACCOUNT_ID)).thenReturn(Optional.of(accAccount));
        when(authenticationService.getCurrentCustomerId()).thenReturn(CURRENT_CUSTOMER_ID);

        AccAccount result = accAccountEntityService.getByIdWithControl(ACCOUNT_ID);

        assertEquals(accAccount, result);
    }

    @Test
    void shouldNotGetByIdWithControl_WhenAccount_BelongsToAnotherCustomer() {

        AccAccount accAccount = new AccAccount();
        accAccount.setCustomerId(OTHER_CUSTOMER_ID);

        when(accAccountDao.findById(ACCOUNT_ID)).thenReturn(Optional.of(accAccount));
        when(authenticationService.getCurrentCustomerId()).thenReturn(CURRENT_CUSTOMER_ID);

        assertThrows(UnauthorizedAccessException.class,
                () -> accAccountEntityService.getByIdWithControl(ACCOUNT_ID));
    }

    @Test
    void shouldNotGetByIdWithControl_WhenAccount_DoesNotExist() {

        when(accAccountDao.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> accAccountEntityService.getByIdWithControl(ACCOUNT_ID));
    }

    @Test
    void shouldGetByIdWithoutOwnerControl_WhenAccount_BelongsToAnotherCustomer() {

        AccAccount accAccount = new AccAccount();
        accAccount.setCustomerId(OTHER_CUSTOMER_ID);

        when(accAccountDao.findById(ACCOUNT_ID)).thenReturn(Optional.of(accAccount));

        AccAccount result = accAccountEntityService.getByIdWithoutOwnerControl(ACCOUNT_ID);

        assertEquals(accAccount, result);
        verifyNoInteractions(authenticationService);
    }

    @Test
    void shouldNotGetByIdWithControl_WhenEntity_HasNoResolvableOwner() {

        AccAccountActivityEntityService accAccountActivityEntityService =
                new AccAccountActivityEntityService(accAccountActivityDao);
        accAccountActivityEntityService.setAuthenticationService(authenticationService);

        when(accAccountActivityDao.findById(ACCOUNT_ID)).thenReturn(Optional.of(new AccAccountActivity()));

        assertThrows(UnauthorizedAccessException.class,
                () -> accAccountActivityEntityService.getByIdWithControl(ACCOUNT_ID));
    }
}
