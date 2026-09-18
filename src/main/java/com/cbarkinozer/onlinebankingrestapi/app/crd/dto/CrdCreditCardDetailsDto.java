package com.cbarkinozer.onlinebankingrestapi.app.crd.dto;

import com.cbarkinozer.onlinebankingrestapi.app.gen.util.StringUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CrdCreditCardDetailsDto {

    private final String customerName;
    private final String customerSurname;
    private final String cardNo;
    private final LocalDate expireDate;
    private final BigDecimal currentDebt;
    private final BigDecimal minimumPaymentAmount;
    private final LocalDate cutoffDate;
    private final LocalDate dueDate;
    private List<CrdCreditCardActivityDto> crdCreditCardActivityDtoList;

    public CrdCreditCardDetailsDto(String customerName, String customerSurname, Long cardNo, LocalDate expireDate,
                                   BigDecimal currentDebt, BigDecimal minimumPaymentAmount,
                                   LocalDate cutoffDate, LocalDate dueDate) {

        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.cardNo = StringUtil.maskCardNo(cardNo);
        this.expireDate = expireDate;
        this.currentDebt = currentDebt;
        this.minimumPaymentAmount = minimumPaymentAmount;
        this.cutoffDate = cutoffDate;
        this.dueDate = dueDate;
    }
}
