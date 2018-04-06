package com.morrisons.base.dropwizard.services;

import com.morrisons.base.dropwizard.dao.CustomerDao;
import com.morrisons.base.dropwizard.exceptions.ApplicationException;
import com.morrisons.base.dropwizard.exceptions.ErrorSequences;
import com.morrisons.base.dropwizard.model.Bank;
import com.morrisons.base.dropwizard.model.Banks;

import javax.inject.Inject;
import java.util.List;

public class BankService {

    private CustomerDao customerDao;

    @Inject
    public BankService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Banks getBanks(int sortcode) {

        List<Bank> bankList = customerDao.getBanks(sortcode);
        if (bankList.isEmpty()) {
            throw new ApplicationException(ErrorSequences.NO_TRANSACTIONS_FOUND);
        }
        return new Banks(bankList);
    }

    public void addBank(Bank bank) {
        customerDao.addBank(bank);
    }
}
