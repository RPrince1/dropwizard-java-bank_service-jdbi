package com.morrisons.base.dropwizard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class CustomerTransactions {

    private List<CustomerTransaction> transactions;
}
