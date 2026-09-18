package com.cbarkinozer.onlinebankingrestapi.app.gen.util;

import com.cbarkinozer.onlinebankingrestapi.app.gen.enums.GenErrorMessage;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.GenBusinessException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;

public class StringUtil {

    private static final int VISIBLE_CARD_NO_DIGIT_COUNT = 4;

    public static Long getRandomNumber(int charCount){

        String randomNumeric;
        do {
            randomNumeric = getRandomNumberAsString(charCount);
        } while (randomNumeric.startsWith("0"));

        Long randomLong = null;
        if (StringUtils.hasText(randomNumeric)){
            randomLong = Long.parseLong(randomNumeric);
        }

        return randomLong;
    }

    public static String getRandomNumberAsString(int charCount){

        validateCharCount(charCount);

        String randomNumeric = RandomStringUtils.randomNumeric(charCount);

        return randomNumeric;
    }

    public static String getRandomString(int charCount){

        validateCharCount(charCount);

        String randomAlphabetic = RandomStringUtils.randomAlphabetic(charCount);

        return randomAlphabetic;
    }

    public static String maskCardNo(Long cardNo){

        if (cardNo == null){
            return null;
        }

        String cardNoAsString = String.valueOf(cardNo);

        if (cardNoAsString.length() <= VISIBLE_CARD_NO_DIGIT_COUNT){
            return "*".repeat(cardNoAsString.length());
        }

        String lastDigits = cardNoAsString.substring(cardNoAsString.length() - VISIBLE_CARD_NO_DIGIT_COUNT);

        return "*".repeat(cardNoAsString.length() - VISIBLE_CARD_NO_DIGIT_COUNT) + lastDigits;
    }

    private static void validateCharCount(int charCount) {
        if (charCount < 0){
            throw new GenBusinessException(GenErrorMessage.VALUE_CANNOT_BE_NEGATIVE);
        }
    }
}
