package com.example.transactionprocessing.dto;

import com.example.transactionprocessing.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private String accountFrom;
    private String accountTo;
    private BigDecimal amount;
    private TransactionStatus status;
    private LocalDateTime createdAt;

    public TransactionResponse(Long id, String accountFrom, String accountTo,
                               BigDecimal amount, TransactionStatus status,
                               LocalDateTime createdAt) {
        this.id = id;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getAccountFrom() {
        return accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
