package com.bhca.account.service;

import com.bhca.account.configuration.transactions.TransactionServiceConfigurationProperties;
import lombok.AllArgsConstructor;
import org.openapitools.client.model.CreateTransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.function.Supplier;

@AllArgsConstructor
@Service
public class TransactionProxyService {

    private final Logger logger = LoggerFactory.getLogger(TransactionProxyService.class);

    private final RestTemplate restTemplate;

    private final TransactionServiceConfigurationProperties props;

    /**
     * Api uri's
     */
    private static class Api {

        private static final String CREATE_TRANSACTION = "/api/v1/transaction/create";
    }

    public void createTransaction(UUID account, BigDecimal amount) {
        String url = props.host + Api.CREATE_TRANSACTION;
        withLogging(() ->
                restTemplate.postForEntity(url, new CreateTransactionRequest().account(account)
                        .amount(amount), Void.class), url);
    }

    private <T> T withLogging(Supplier<T> func, String url) {
        try {
            return func.get();
        } catch (Throwable e) {
            logger.error(MessageFormat.format("Failed to call URL {0}", url), e);
            throw e;
        }
    }
}
