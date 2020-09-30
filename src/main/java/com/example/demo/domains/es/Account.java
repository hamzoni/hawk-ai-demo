package com.example.demo.domains.es;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class Account implements Serializable {

    @Id
    String accountNumber;
    String holder;
}
