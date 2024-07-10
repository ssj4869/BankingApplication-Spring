package com.example.BankingApp.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;

}
