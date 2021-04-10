package com.bhca.account.configuration.transactions;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@AllArgsConstructor
@ConfigurationProperties("service.transaction")
public class TransactionServiceConfigurationProperties {
    public final String host;
}