package com.bhca.account.service;

import com.bhca.account.configuration.cache.CachesConfig;
import com.bhca.account.db.Account;
import com.bhca.account.db.AccountSequenceNumber;
import com.bhca.account.db.AccountStatus;
import com.bhca.account.generator.AccountNumberGenerator;
import com.bhca.account.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.openapitools.client.model.AccountData;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Account service. Works with customer accounts
 */
@AllArgsConstructor
@Service
public class AccountService {

    private final AccountRepository repository;

    private final AccountNumberGenerator accountNumberGenerator;

    private static final int ACCOUNT_DATA_DEFAULT_SIZE = 50;

    @CacheEvict(value = CachesConfig.ACCOUNT_CACHE, key = "#customerId", beforeInvocation = true)
    @Transactional
    public Account createAccount(UUID customerId, BigDecimal initialCredit) {
        Account account = new Account();
        account.setBalance(initialCredit);
        account.setAccountNumber(new AccountSequenceNumber());
        account.setStatus(AccountStatus.INCOMPLETE);
        account.setCustomer(customerId);
        repository.save(account);
        return account;
    }

    @CacheEvict(value = CachesConfig.ACCOUNT_CACHE, key = "#account.customer", beforeInvocation = true)
    @Transactional
    public void setCompleted(Account account) {
        account.setStatus(AccountStatus.COMPLETE);
        account.setClientNumber(accountNumberGenerator.generate(account.getAccountNumber().getNumber().toString()));
        repository.save(account);
    }

    @Cacheable(value = CachesConfig.ACCOUNT_CACHE, key = "#customerId")
    @Transactional(readOnly = true)
    public List<Account> getAccountVerboseData(UUID customerId, Integer page, Integer size) {
        return repository.findByCustomer(customerId,
                PageRequest.of(page == null ? 0 : page, size == null ? ACCOUNT_DATA_DEFAULT_SIZE : size,
                        Sort.by(Sort.Direction.DESC, "createdAt")));
    }
}
