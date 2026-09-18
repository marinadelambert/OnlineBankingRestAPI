package com.cbarkinozer.onlinebankingrestapi.app.gen.util;

import com.cbarkinozer.onlinebankingrestapi.app.gen.enums.GenErrorMessage;
import com.cbarkinozer.onlinebankingrestapi.app.gen.exceptions.GenBusinessException;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;

public class StringUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

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

        return randomFromAlphabet(charCount, DIGITS);
    }

    public static String getRandomString(int charCount){

        validateCharCount(charCount);

        return randomFromAlphabet(charCount, LETTERS);
    }

    private static String randomFromAlphabet(int charCount, String alphabet) {
        StringBuilder sb = new StringBuilder(charCount);
        for (int i = 0; i < charCount; i++) {
            sb.append(alphabet.charAt(SECURE_RANDOM.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private static void validateCharCount(int charCount) {
        if (charCount < 0){
            throw new GenBusinessException(GenErrorMessage.VALUE_CANNOT_BE_NEGATIVE);
        }
    }
}
