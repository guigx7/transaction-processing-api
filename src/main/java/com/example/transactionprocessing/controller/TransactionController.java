package com.example.transactionprocessing.controller;

import com.example.transactionprocessing.dto.CreateTransactionRequest;
import com.example.transactionprocessing.dto.TransactionResponse;
import com.example.transactionprocessing.model.Transaction;
import com.example.transactionprocessing.service.TransactionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public TransactionResponse create(@RequestBody CreateTransactionRequest request) {

        Transaction transaction = transactionService.createTransaction(request);

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountFrom(),
                transaction.getAccountTo(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );
    }
}
