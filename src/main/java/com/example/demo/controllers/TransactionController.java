package com.example.demo.controllers;

import com.example.demo.constants.QueueEvents;
import com.example.demo.constants.WsConstants;
import com.example.demo.domains.es.Transaction;
import com.example.demo.domains.kafka.Message;
import com.example.demo.services.core.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "type", defaultValue = QueueEvents.PUT_TRANSACTION, required = false) String type
    ) {
        var pageable = PageRequest.of(page, size, Sort.by("recordDate").ascending());
        return transactionService.listTransactions(pageable, type);
    }

    // WebSocket
    @MessageMapping(WsConstants.WS_ENDPOINT + "/" + QueueEvents.PUT_TRANSACTION)
    @SendTo(WsConstants.WS_ENDPOINT + "/" + QueueEvents.PUT_TRANSACTION)
    public Message wsPutTransaction(@Payload Message message) {
        log.info("Receiving message  " + QueueEvents.PUT_TRANSACTION);
        return message;
    }

    @MessageMapping(WsConstants.WS_ENDPOINT + "/" + QueueEvents.ACTIVATE_DORMANT)
    @SendTo(WsConstants.WS_ENDPOINT + "/" + QueueEvents.ACTIVATE_DORMANT)
    public Message wsActivateDormant(@Payload Message message) {
        log.info("Receiving message " + QueueEvents.ACTIVATE_DORMANT);
        return message;
    }
}
