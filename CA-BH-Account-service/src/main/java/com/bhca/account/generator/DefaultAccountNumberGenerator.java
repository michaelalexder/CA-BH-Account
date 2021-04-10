package com.bhca.account.generator;

import org.springframework.stereotype.Component;

@Component
public class DefaultAccountNumberGenerator implements AccountNumberGenerator {

    @Override
    public String generate() {
        return "";
    }
}
