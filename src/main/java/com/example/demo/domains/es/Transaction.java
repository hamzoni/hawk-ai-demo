package com.example.demo.domains.es;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode
@Document(indexName = "transaction")
public class Transaction implements Serializable {
    @Id
    String bankTransactionId;

    @Field(type = FieldType.Nested, includeInParent = true)
    Account receivingAccount;

    @Field(type = FieldType.Nested, includeInParent = true)
    Account sendingAccount;

    Long amount;
    String currency;
    Date bankProcessingTimestamp;
    String usage;

    Date recordDate = new Date();
}
