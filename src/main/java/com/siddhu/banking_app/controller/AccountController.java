package com.siddhu.banking_app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.siddhu.banking_app.Views;
import com.siddhu.banking_app.dto.AccountDto;
import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.exceptions.AccountBlocked;
import com.siddhu.banking_app.exceptions.NotEnoughMoney;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.response.ApiResponse;
import com.siddhu.banking_app.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("${api.prefix}/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Add Account REST API
    @PostMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    // Get account REST API
    @GetMapping("/{id}")
    @JsonView(Views.Full.class)
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id){
        AccountDto accountDto = accountService.getAccountById(id);
        return ResponseEntity.ok(accountDto);
    }

    @GetMapping("/account_number/{accountNumber}")
    @JsonView(Views.Full.class)
    public ResponseEntity<AccountDto> getAccountByAccountNumber(@PathVariable String accountNumber){
        AccountDto accountDto = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(accountDto);
    }

    // Get account REST API
    @GetMapping("/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        List<AccountDto> accountDtos = accountService.getAllAccounts();
        return ResponseEntity.ok(accountDtos);
    }

    // Deposit REST API
    @PutMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request){
        try {
            Double amount = request.get("amount");
            TransactionCreateDto accountDto = accountService.deposit(id,amount);
            return ResponseEntity.ok(new ApiResponse("Success!",accountDto));
        } catch (ResourceNotFoundException |IllegalArgumentException| AccountBlocked | NotEnoughMoney e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@PathVariable Long id,@RequestBody Map<String,Double> request){
        try {
            Double amount = request.get("amount");
            TransactionCreateDto accountDto = accountService.withdraw(id,amount);
            return ResponseEntity.ok(new ApiResponse("success!",accountDto));
        } catch (NotEnoughMoney |IllegalArgumentException| ResourceNotFoundException | AccountBlocked e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponse> getBalance(@PathVariable Long id){
        try {
            Double amount = accountService.getBalance(id);
            return ResponseEntity.ok(new ApiResponse("Success!",amount));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long id){
        try {
            accountService.deleteAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Deleted!",null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/deactivate")
    public ResponseEntity<ApiResponse> deactivateAccountByAccountNumber(@RequestParam String accountNumber){
        try {
            accountService.deactivateAccountByAccount(accountNumber);
            return ResponseEntity.ok(new ApiResponse("Deactivated!",null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/reactivate")
    public ResponseEntity<ApiResponse> reactivateAccountByAccountNumber(@RequestParam String accountNumber){
        try {
            accountService.reactivateAccountByAccount(accountNumber);
            return ResponseEntity.ok(new ApiResponse("Reactivated!",null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
