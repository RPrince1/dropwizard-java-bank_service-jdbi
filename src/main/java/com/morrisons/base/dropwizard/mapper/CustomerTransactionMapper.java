package com.morrisons.base.dropwizard.mapper;

import com.morrisons.base.dropwizard.model.CustomerTransaction;
import lombok.NoArgsConstructor;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor
public class CustomerTransactionMapper implements ResultSetMapper<CustomerTransaction> {

    public CustomerTransaction map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        return new CustomerTransaction(
                UUID.fromString(resultSet.getString("transaction_id")),
                resultSet.getString("account_id"),
                resultSet.getBigDecimal("amount")
        );
    }
}

