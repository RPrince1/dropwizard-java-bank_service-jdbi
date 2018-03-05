package com.morrisons.base.dropwizard.dao;

import com.morrisons.base.dropwizard.mapper.CustomerTransactionMapper;
import com.morrisons.base.dropwizard.model.CustomerTransaction;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

public interface CustomerDao {

    String allFields = "transaction_id, account_id, amount ";
    String allValues = ":ct.transactionId, :ct.accountId, :ct.amount ";
    String tableName = "customer_transactions ";

    @Mapper(CustomerTransactionMapper.class)
    @SqlQuery("SELECT " +
            allFields +
            "FROM " +
            tableName)
    List<CustomerTransaction> getCustomerTransactions(@Bind("accountId") String accountId);


    @SqlUpdate("INSERT INTO " +
            tableName +
            "( " +
            allFields +
            ") VALUES (" +
            allValues +
            ")")
    void addTransaction(@BindBean("ct") CustomerTransaction customerTransaction);

}
