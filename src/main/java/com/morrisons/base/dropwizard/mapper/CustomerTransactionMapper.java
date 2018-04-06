package com.morrisons.base.dropwizard.mapper;

import com.morrisons.base.dropwizard.model.Bank;
import lombok.NoArgsConstructor;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@NoArgsConstructor
public class CustomerTransactionMapper implements ResultSetMapper<Bank> {

    public Bank map(int index, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        return new Bank(
                resultSet.getString("name"),
                resultSet.getString("address"),
                resultSet.getInt("sortcode"),
                resultSet.getInt("branch_number")
        );
    }
}

