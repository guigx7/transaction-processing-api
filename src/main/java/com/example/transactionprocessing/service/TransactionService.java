package com.example.transactionprocessing.service;

import com.example.transactionprocessing.dto.CreateTransactionRequest;
import com.example.transactionprocessing.enums.TransactionStatus;
import com.example.transactionprocessing.model.Transaction;
import com.example.transactionprocessing.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {

        Long transactionId = repository.createTransaction(
                request.getAccountFrom(),
                request.getAccountTo(),
                request.getAmount()
        );

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAccountFrom(request.getAccountFrom());
        transaction.setAccountTo(request.getAccountTo());
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }
}
