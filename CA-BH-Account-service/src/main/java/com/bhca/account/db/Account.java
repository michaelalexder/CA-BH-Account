package com.bhca.account.db;

import com.bhca.common.AbstractBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Account extends AbstractBaseEntity {

    @Column(nullable = false)
    private UUID customer;

    @OneToOne(cascade = CascadeType.ALL)
    private AccountSequenceNumber accountNumber;

    @Column(unique = true)
    private String clientNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
