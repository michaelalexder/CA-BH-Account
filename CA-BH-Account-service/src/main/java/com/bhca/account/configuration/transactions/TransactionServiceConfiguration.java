package com.bhca.account.configuration.transactions;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TransactionServiceConfigurationProperties.class)
public class TransactionServiceConfiguration {
}
