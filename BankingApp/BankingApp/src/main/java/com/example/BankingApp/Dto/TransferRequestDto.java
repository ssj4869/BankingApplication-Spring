package com.example.BankingApp.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {
    private String sourceAccountId;
    private String destinationAccountId;
    private Float amount;

}
