package com.example.demo.services.core;

import com.example.demo.constants.QueueEvents;
import com.example.demo.domains.es.DormantAccount;
import com.example.demo.domains.es.Transaction;
import com.example.demo.domains.kafka.Message;
import com.example.demo.repositories.DormantAccountRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.services.kafka.KafkaPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${spring.application.name}")
    public static String APP_NAME;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    DormantAccountRepository dormantAccountRepository;

    @Autowired
    KafkaPublisherService kafkaService;

    @Override
    @Transactional
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
        lastSenderTransaction.ifPresent(value -> checkDormantAccountActivation(value, false));

        // store the transaction
        transaction = transactionRepository.save(transaction);

        // trigger notification new transactions
        var message = Message.builder().content(transaction).build();
        kafkaService.sendMessage(QueueEvents.PUT_TRANSACTION, message);
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
        activateDormantAccount(dormantAccount);
//        }
    }

    private void activateDormantAccount(DormantAccount dormantAccount) {
        dormantAccountRepository.save(dormantAccount);
        var message = Message.builder().content(dormantAccount).build();
        kafkaService.sendMessage(QueueEvents.ACTIVATE_DORMANT, message);
    }

    @Override
    public Page listTransactions(Pageable page, String type) {
        if (type.equals(QueueEvents.PUT_TRANSACTION)) {
            return transactionRepository.findAll(page);
        }
        return dormantAccountRepository.findAll(page);
    }
}
