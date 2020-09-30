package com.example.demo.controllers;

import com.example.demo.constants.QueueEvents;
import com.example.demo.domains.es.Transaction;
import com.example.demo.domains.kafka.Message;
import com.example.demo.services.core.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    // HTTP
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

    // WebSocket
    @MessageMapping("/" + QueueEvents.PUT_TRANSACTION)
    @SendTo(QueueEvents.PUT_TRANSACTION)
    public Message wsPutTransaction(@Payload Message message) {
        return message;
    }

    @MessageMapping("/" + QueueEvents.ACTIVATE_DORMANT)
    @SendTo("/" + QueueEvents.ACTIVATE_DORMANT)
    public Message wsActivateDormant(@Payload Message message) {
        return message;
    }
}
