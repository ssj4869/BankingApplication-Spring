package com.example.BankingApp.Controller;

import com.example.BankingApp.Dto.LoginUserDto;
import com.example.BankingApp.Dto.RegisterUserDto;
import com.example.BankingApp.Repository.AccountDetailsRepository;
import com.example.BankingApp.Repository.UserRepository;
import com.example.BankingApp.model.AccountDetails;
import com.example.BankingApp.model.User;
import com.example.BankingApp.Responses.LoginResponse;
import com.example.BankingApp.Service.AuthenticationService;
import com.example.BankingApp.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    @Autowired
    private UserRepository userRepo;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        Optional<User> userCheck = userRepo.findById(registerUserDto.getEmail());

        if(userCheck.isPresent()) {
            //user already exists!!
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            try {
                User registeredUser = authenticationService.signup(registerUserDto);
                System.out.println(registeredUser);

                return new ResponseEntity<>(registeredUser, HttpStatus.OK);

            }catch (Exception e) {
                System.out.println(e);
            }
        }

        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
