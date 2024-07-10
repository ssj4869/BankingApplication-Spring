package com.example.BankingApp.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Table("transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {

    @PrimaryKey
    @Column("transaction_id")
    private UUID transactionId;

    private String email;

    @Column("transaction_type")
    private String transactionType;

    private Float amount;

    @Column("transaction_date")
    private Instant transactionDate;

    @Column("transaction_to")
    private String transactionTo;

}