package com.bhca.account.db;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Setter
@Getter
@Entity
public class AccountSequenceNumber {

    @Id
    @GeneratedValue
    private Long number;
}