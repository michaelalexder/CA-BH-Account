package com.bhca.account.controller;

import com.bhca.account.service.AccountTransactionService;
import lombok.AllArgsConstructor;
import org.openapitools.client.model.AccountData;
import org.openapitools.client.model.CreateAccountRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1/account")
@RestController
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountTransactionService accountTransactionService;

    @PostMapping
    public void create(@RequestBody CreateAccountRequest request) {
        logger.debug("New account creation is called for customer " + request.getCustomerId());
        accountTransactionService.createAccountWithTransaction(request.getCustomerId(), request.getInitialCredit());
        logger.debug("New Account created");
    }

    @GetMapping("/{customerId}")
    public List<AccountData> customerAccounts(@PathVariable UUID customerId, @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer size) {
        logger.debug("Retrieving accounts for customer " + customerId);
        return accountTransactionService.accountsWithTransactions(customerId, page, size);
    }
}
