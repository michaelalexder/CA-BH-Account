package com.bhca.account.generator;

import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccountNumberGenerator implements AccountNumberGenerator {

    @Value("${bank.code}")
    private String bankCode;

    @Value("${bank.ncd}")
    private String ncd;

    private static final int IBAN_TOTAL_SIZE = 12;

    @Override
    public String generate(String accountNumber) {
        return new Iban.Builder()
                .countryCode(CountryCode.BE)
                .bankCode(bankCode)
                .accountNumber(padWithZeros(accountNumber))
                .nationalCheckDigit(ncd)
                .build().toFormattedString();
    }

    private String padWithZeros(String value) {
        int paddingSize = IBAN_TOTAL_SIZE - bankCode.length();
        if (value.length() < paddingSize) {
            StringBuilder sb = new StringBuilder();
            for (int i = value.length() + 1; i < paddingSize; i++) {
                sb.append("0");
            }
            return sb + value;
        }
        return value;
    }
}
