package com.bhca.account.repository;

import com.bhca.account.db.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByCustomer(UUID customerId, Pageable pageable);
}
