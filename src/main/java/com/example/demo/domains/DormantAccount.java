package com.example.demo.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(indexName = "dormant_account")
public class DormantAccount extends Account {
    @Field(type = FieldType.Nested, includeInParent = true)
    Transaction transaction;

    Boolean isReceiver;
}
