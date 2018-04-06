package com.morrisons.base.dropwizard.dao;

import com.morrisons.base.dropwizard.mapper.CustomerTransactionMapper;
import com.morrisons.base.dropwizard.model.Bank;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

public interface CustomerDao {

    String allFields = "name, address, sortcode, branch_number";
    String allValues = ":ct.name, :ct.address, :ct.sortcode, :ct.branchNumber";
    String tableName = "bank";

    /*
    String allFields = "transaction_id, account_id, amount ";
    String allValues = ":ct.transactionId, :ct.accountId, :ct.amount ";
    String tableName = "customer_transactions ";
*/

    @Mapper(CustomerTransactionMapper.class)
    @SqlQuery("SELECT " + allFields +  " FROM " + tableName + " WHERE sortcode = :sortcode")
    List<Bank> getBanks(@Bind("sortcode") int sortcode);


    @SqlUpdate("INSERT INTO " +
            tableName +
            "( " +
            allFields +
            ") VALUES (" +
            allValues +
            ")")
    void addBank(@BindBean("ct") Bank bank);

}
