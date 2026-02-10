package com.example.transactionprocessing.service;

import com.example.transactionprocessing.dto.CreateTransactionRequest;
import com.example.transactionprocessing.enums.TransactionStatus;
import com.example.transactionprocessing.model.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransactionService {

    private final AtomicLong idGenerator = new AtomicLong(1);

    public Transaction createTransaction(CreateTransactionRequest request) {

        Transaction transaction = new Transaction();
        transaction.setId(idGenerator.getAndIncrement());
        transaction.setAccountFrom(request.getAccountFrom());
        transaction.setAccountTo(request.getAccountTo());
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }
}
