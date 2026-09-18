package com.cbarkinozer.onlinebankingrestapi.app.acc.dto;

import com.cbarkinozer.onlinebankingrestapi.app.acc.enums.AccAccountType;
import com.cbarkinozer.onlinebankingrestapi.app.acc.enums.AccCurrencyType;
import lombok.Data;

@Data
public class AccAccountSaveDto {

    private AccCurrencyType currencyType;
    private AccAccountType accountType;
}
