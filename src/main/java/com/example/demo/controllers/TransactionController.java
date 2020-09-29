package com.example.demo.controllers;

import com.example.demo.domains.es.Transaction;
import com.example.demo.services.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    private final
    TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    public Object putTransaction(
            @RequestBody Transaction transaction
    ) {
        transactionService.putTransaction(transaction);
        return transaction;
    }

    @GetMapping()
    public Object listTransactions(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size
    ) {
        var pageable = PageRequest.of(page, size);
        return transactionService.listTransactions(pageable);
    }
}
