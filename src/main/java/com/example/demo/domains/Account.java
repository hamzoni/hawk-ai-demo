package com.example.demo.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@EqualsAndHashCode
public class Account implements Serializable {

    @Id
    String accountNumber;
    String holder;
}
