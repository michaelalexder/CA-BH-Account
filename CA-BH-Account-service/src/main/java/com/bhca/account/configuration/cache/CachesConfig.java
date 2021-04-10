package com.bhca.account.configuration.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CachesConfig {

    public static final String ACCOUNT_CACHE = "account";
}