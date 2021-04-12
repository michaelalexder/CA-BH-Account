package com.bhca.account.service;

import com.bhca.account.db.Account;
import lombok.AllArgsConstructor;
import org.openapitools.client.model.AccountData;
import org.openapitools.client.model.AccountTransactionInfo;
import org.openapitools.client.model.TransactionItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Combines accounts logic with transactions
 */
@AllArgsConstructor
@Service
public class AccountTransactionService {

    private final Logger logger = LoggerFactory.getLogger(AccountTransactionService.class);

    private final AccountService accountService;

    private final TransactionApiClient transactionApiClient;

    public void createAccountWithTransaction(UUID customerId, BigDecimal initialCredit) {
        if (initialCredit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial credit must be greater or equals to zero");
        }
        Account account = accountService.createAccount(customerId, initialCredit);
        logger.debug("Created new account {} in {} state", account.getClientNumber(), account.getStatus());
        if (initialCredit.compareTo(BigDecimal.ZERO) > 0) {
            transactionApiClient.createTransaction(account.getId(), initialCredit);
            logger.debug("Transaction is created for customer {} with amount of {}", customerId, initialCredit);
        }
        accountService.setCompleted(account);
        logger.info("New account {} created for customer {}", account.getClientNumber(), customerId);
    }

    public List<AccountData> accountsWithTransactions(UUID customerId, Integer page, Integer size) {
        List<Account> accountData = accountService.getAccountVerboseData(customerId, page, size);
        Map<UUID, List<TransactionItem>> accountTransactions = transactionApiClient.transactions(accountData.stream()
                .map(Account::getId).collect(Collectors.toList())).stream()
                .collect(Collectors.groupingBy(TransactionItem::getAccount));
        return accountData.stream().map(account ->
                new AccountData().id(account.getId())
                .balance(account.getBalance())
                .number(account.getClientNumber())
                .transactions(
                        accountTransactions.getOrDefault(account.getId(), Collections.emptyList())
                                .stream()
                                .map(transactionItem -> new AccountTransactionInfo()
                                        .amount(transactionItem.getAmount()).createdAt(transactionItem.getCreatedAt())
                                ).collect(Collectors.toList())
                )
        ).collect(Collectors.toList());
    }
}
