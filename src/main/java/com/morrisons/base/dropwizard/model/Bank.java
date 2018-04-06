package com.morrisons.base.dropwizard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
public class Bank {

    private String name;
    private String address;
    private int sortcode;
    private int branchNumber;

    /*
    private UUID transactionId;
    private String accountId;
    private BigDecimal amount;
    */
}
