package com.morrisons.base.dropwizard.services;

import com.morrisons.base.dropwizard.dao.CustomerDao;
import com.morrisons.base.dropwizard.exceptions.ApplicationException;
import com.morrisons.base.dropwizard.exceptions.ErrorSequences;
import com.morrisons.base.dropwizard.model.CustomerTransaction;
import com.morrisons.base.dropwizard.model.CustomerTransactions;

import javax.inject.Inject;
import java.util.List;

public class CustomerService {

    private CustomerDao customerDao;

    @Inject
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public CustomerTransactions getCustomerTransactions(String accountId) {

        List<CustomerTransaction> transactionList = customerDao.getCustomerTransactions(accountId);
        if (transactionList.isEmpty()) {
            throw new ApplicationException(ErrorSequences.NO_TRANSACTIONS_FOUND);
        }
        return new CustomerTransactions(transactionList);
    }

    public void addTransaction(CustomerTransaction transaction) {
        customerDao.addTransaction(transaction);
    }
}
