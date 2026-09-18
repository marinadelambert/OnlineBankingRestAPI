package com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions;

import com.cbarkinozer.onlinebankingrestapi.app.gen.enums.BaseErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends GenBusinessException{

    public UnauthorizedAccessException(BaseErrorMessage message) {
        super(message);
    }
}
