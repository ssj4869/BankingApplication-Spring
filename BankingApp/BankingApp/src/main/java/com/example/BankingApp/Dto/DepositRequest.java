package com.example.BankingApp.Dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

public class DepositRequest {
    private String email;
    private Float amount;


}
