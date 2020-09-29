package com.example.demo.services;

import com.example.demo.domains.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {
    void putTransaction(Transaction transaction);

    Page listTransactions(Pageable page);
}
