package com.example.transactionprocessing.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;

@Repository
public class TransactionRepository {

    private final SimpleJdbcCall createTransactionCall;
    private final SimpleJdbcCall updateStatusCall;

    public void updateTransactionStatus(Long transactionId, String status) {

        updateStatusCall.execute(
                new MapSqlParameterSource()
                        .addValue("P_TRANSACTION_ID", transactionId)
                        .addValue("P_STATUS", status)
        );
    }

    public Long createTransaction(
            String accountFrom,
            String accountTo,
            BigDecimal amount
    ) {
        Map<String, Object> result = createTransactionCall.execute(
                new MapSqlParameterSource()
                        .addValue("P_ACCOUNT_FROM", accountFrom)
                        .addValue("P_ACCOUNT_TO", accountTo)
                        .addValue("P_AMOUNT", amount)
        );

        System.out.println("PL/SQL result map: " + result);

        Object id = result.get("P_TRANSACTION_ID");

        if (id == null) {
            throw new IllegalStateException(
                    "PL/SQL procedure CREATE_TRANSACTION was not executed or returned null ID"
            );
        }

        return ((Number) id).longValue();
    }



    public TransactionRepository(DataSource dataSource) {

        this.createTransactionCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("TXN_USER")
                .withCatalogName("PKG_TRANSACTION")
                .withProcedureName("CREATE_TRANSACTION")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_ACCOUNT_FROM", Types.VARCHAR),
                        new SqlParameter("P_ACCOUNT_TO", Types.VARCHAR),
                        new SqlParameter("P_AMOUNT", Types.NUMERIC),
                        new SqlOutParameter("P_TRANSACTION_ID", Types.NUMERIC)
                );

        this.updateStatusCall = new SimpleJdbcCall(dataSource)
                .withSchemaName("TXN_USER")
                .withCatalogName("PKG_TRANSACTION")
                .withProcedureName("UPDATE_TRANSACTION_STATUS")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("P_TRANSACTION_ID", Types.NUMERIC),
                        new SqlParameter("P_STATUS", Types.VARCHAR)
                );
    }
}