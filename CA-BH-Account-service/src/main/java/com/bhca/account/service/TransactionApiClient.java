package com.bhca.account.service;

import com.bhca.account.configuration.transactions.TransactionServiceConfigurationProperties;
import com.bhca.account.filter.MdcParams;
import org.openapitools.client.model.CreateTransactionRequest;
import org.openapitools.client.model.TransactionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class TransactionApiClient {

    private final Logger logger = LoggerFactory.getLogger(TransactionApiClient.class);

    private final RestTemplate restTemplate;

    private final TransactionServiceConfigurationProperties props;

    public TransactionApiClient(RestTemplateBuilder builder, TransactionServiceConfigurationProperties props) {
        this.restTemplate = builder.build();
        this.props = props;
    }

    /**
     * Api uri's
     */
    private static class Api {

        private static final String CREATE_TRANSACTION = "/api/v1/transaction/create";
        private static final String ACCOUNTS_LIST = "/api/v1/transaction";
    }

    private <T> HttpEntity<T> defaultHeaders(T body, Consumer<HttpHeaders> headersConsumer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(MdcParams.REQUEST_ID, MDC.get(MdcParams.REQUEST_ID));
        if (headersConsumer != null) {
            headersConsumer.accept(headers);
        }
        return new HttpEntity<>(body, headers);
    }

    public void createTransaction(UUID account, BigDecimal amount) {
        String url = props.host + Api.CREATE_TRANSACTION;
        withLogging(() ->
                restTemplate.postForEntity(url, defaultHeaders(new CreateTransactionRequest().account(account)
                        .amount(amount), null), Void.class), url);
    }

    public List<TransactionItem> transactions(List<UUID> accountsList) {
        String url = props.host + Api.ACCOUNTS_LIST;
        return (List<TransactionItem>) withLogging(() ->
                restTemplate.postForEntity(url, defaultHeaders("", null), List.class), url);
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
