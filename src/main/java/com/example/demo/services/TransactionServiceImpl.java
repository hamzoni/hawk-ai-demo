package com.example.demo.services;

import com.example.demo.domains.DormantAccount;
import com.example.demo.domains.Transaction;
import com.example.demo.repositories.DormantAccountRepository;
import com.example.demo.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DormantAccountRepository dormantAccountRepository;

    @Override
    public void putTransaction(Transaction transaction) {

        // check if the transaction is activation of dormant account, applied for both receiver and sender
        var receiverAcc = transaction.getReceivingAccount();
        var senderAcc = transaction.getSendingAccount();
        var receiverNumber = receiverAcc.getAccountNumber();
        var senderNumber = senderAcc.getAccountNumber();

        // find last transaction
        Pageable pageable = PageRequest.of(0, 1, Sort.Direction.DESC, "bankProcessingTimestamp");

        var lastReceiverTransaction = transactionRepository.findByReceivingAccount_AccountNumber(receiverNumber, pageable).stream().findFirst();
        var lastSenderTransaction = transactionRepository.findBySendingAccount_AccountNumber(senderNumber, pageable).stream().findFirst();

        lastReceiverTransaction.ifPresent(value -> checkDormantAccountActivation(value, true));
        lastSenderTransaction.ifPresent(value -> checkDormantAccountActivation(value, true));

        // store the transaction
        transactionRepository.save(transaction);
    }

    private void checkDormantAccountActivation(Transaction transaction, boolean isReceiver) {
        var account = isReceiver ? transaction.getReceivingAccount() : transaction.getSendingAccount();

        // prepare dormant account
        var dormantAccount = new DormantAccount();
        dormantAccount.setIsReceiver(isReceiver);
        dormantAccount.setTransaction(transaction);
        dormantAccount.setAccountNumber(account.getAccountNumber());
        dormantAccount.setHolder(account.getHolder());

        // apply dormant check
        // rule #1 - last transaction occurs less than X time compare today
        var targetTime = transaction.getBankProcessingTimestamp();
        var secondsDifferent = (new Date().getTime() - targetTime.getTime()) / 1000;
//        if (secondsDifferent > 5) {
//            dormantAccountRepository.save(dormantAccount);
//        }
        dormantAccountRepository.save(dormantAccount);
    }

    @Override
    public Page listTransactions(Pageable page) {
        return transactionRepository.findAll(page);
    }
}