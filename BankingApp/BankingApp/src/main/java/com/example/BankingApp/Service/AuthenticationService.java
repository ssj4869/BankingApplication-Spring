package com.example.BankingApp.Service;

import com.example.BankingApp.Dto.LoginUserDto;
import com.example.BankingApp.Dto.RegisterUserDto;
import com.example.BankingApp.Repository.AccountDetailsRepository;
import com.example.BankingApp.Repository.UserRepository;
import com.example.BankingApp.model.AccountDetails;
import com.example.BankingApp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    private AccountDetailsRepository adRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {

        try {

            User user = new User();
            user.setFullName(input.getFullName());
            user.setEmail(input.getEmail());
            user.setPassword(passwordEncoder.encode(input.getPassword()));

            AccountDetails ad = new AccountDetails();
            ad.setEmail(input.getEmail());
            ad.setBalance(0.0F);
            adRepository.save(ad);
            userRepository.save(user);


            return user ;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
