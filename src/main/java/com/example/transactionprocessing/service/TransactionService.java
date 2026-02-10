package com.example.transactionprocessing.service;

import com.example.transactionprocessing.client.ExternalProcessorClient;
import com.example.transactionprocessing.dto.CreateTransactionRequest;
import com.example.transactionprocessing.enums.TransactionStatus;
import com.example.transactionprocessing.model.Transaction;
import com.example.transactionprocessing.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final ExternalProcessorClient externalClient;

    public TransactionService(
            TransactionRepository repository,
            ExternalProcessorClient externalClient
    ) {
        this.repository = repository;
        this.externalClient = externalClient;
    }

    public Transaction createTransaction(CreateTransactionRequest request) {

        Long transactionId = repository.createTransaction(
                request.getAccountFrom(),
                request.getAccountTo(),
                request.getAmount()
        );

        boolean approved = externalClient.processTransaction();

        String finalStatus = approved
                ? TransactionStatus.PROCESSED.name()
                : TransactionStatus.FAILED.name();

        repository.updateTransactionStatus(transactionId, finalStatus);

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setAccountFrom(request.getAccountFrom());
        transaction.setAccountTo(request.getAccountTo());
        transaction.setAmount(request.getAmount());
        transaction.setStatus(TransactionStatus.valueOf(finalStatus));
        transaction.setCreatedAt(LocalDateTime.now());

        return transaction;
    }
}
