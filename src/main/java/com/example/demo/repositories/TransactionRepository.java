package com.example.demo.repositories;

import com.example.demo.domains.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TransactionRepository extends ElasticsearchRepository<Transaction, String> {
    Page<Transaction> findByReceivingAccount_AccountNumber(String accountNumber, Pageable page);

    Page<Transaction> findBySendingAccount_AccountNumber(String accountNumber, Pageable page);
}
