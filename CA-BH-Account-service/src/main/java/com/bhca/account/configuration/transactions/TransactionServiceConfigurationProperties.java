package com.bhca.account.configuration.transactions;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("service.transaction")
public class TransactionServiceConfigurationProperties {
    public final String host;
}