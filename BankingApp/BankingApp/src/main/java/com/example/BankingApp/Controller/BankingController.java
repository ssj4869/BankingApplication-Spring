package com.example.BankingApp.Controller;
import com.example.BankingApp.Dto.TransferRequestDto;
import com.example.BankingApp.Dto.DepositRequest;
import com.example.BankingApp.Dto.WithdrawRequest;
import com.example.BankingApp.Service.BankingService;
import com.example.BankingApp.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/banking")
public class BankingController {

    @Autowired
    private BankingService bankingService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest depositRequest) {
        try {
            bankingService.deposit(depositRequest.getAmount());
            return new ResponseEntity<>("Deposited "+depositRequest.getAmount()+" Into Account! ", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequestDto transferRequestDto) {
        String response = bankingService.transfer(transferRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")

    public ResponseEntity<Float> balance() {
        Float balance = bankingService.balance();
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }
    @PostMapping("/withdraw")

    public ResponseEntity<String> withDraw(@RequestBody WithdrawRequest withdrawRequest) {
        try {
            String response = bankingService.withdraw(withdrawRequest.getAmount());
            if(response == "OK") {
                Float balance = bankingService.balance();
                return  new ResponseEntity<>("Withdraw Success. Current Balance: "+balance, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Error while Withdrawing. " +response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/statement")
    public ResponseEntity<List<Transaction>> getStatement() {
        try {
            List<Transaction> statements = bankingService.getStatement();
            return new ResponseEntity<>(statements, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
