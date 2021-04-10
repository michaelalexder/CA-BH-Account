package com.bhca.account.service;

import com.bhca.account.db.Account;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Combines accounts logic with transactions
 */
@AllArgsConstructor
@Service
public class AccountTransactionService {

    private final Logger logger = LoggerFactory.getLogger(AccountTransactionService.class);

    private final AccountService accountService;

    private final TransactionProxyService transactionProxyService;

    public void createAccountWithTransaction(UUID customerId, BigDecimal initialCredit) {
        Account account = accountService.createAccount(customerId, initialCredit);
        logger.debug("Created new account {} in {} state", account.getNumber(), account.getStatus());
        if (initialCredit.compareTo(BigDecimal.ZERO) > 0) {
            transactionProxyService.createTransaction(account.getId(), initialCredit);
            logger.debug("Transaction is created for customer {} with amount of {}", customerId, initialCredit);
        }
        accountService.setCompleted(account);
        logger.info("New account {} created for customer {}", account.getNumber(), customerId);
    }
}
