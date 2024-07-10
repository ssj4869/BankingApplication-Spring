package com.example.BankingApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("account_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AccountDetails {

    @PrimaryKey
    private String email;
    private Float balance;

}