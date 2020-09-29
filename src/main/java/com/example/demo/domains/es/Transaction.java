package com.example.demo.domains.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
