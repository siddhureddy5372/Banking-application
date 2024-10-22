package com.siddhu.banking_app.controller;

import com.siddhu.banking_app.dto.AccountDto;
import com.siddhu.banking_app.exceptions.NotEnoughMoney;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.response.ApiResponse;
import com.siddhu.banking_app.service.AccountService;
import lombok.Getter;
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
    public ResponseEntity<AccountDto> addAccount(@RequestBody AccountDto accountDto){
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    // Get account REST API
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccount(@PathVariable Long id){
        AccountDto accountDto = accountService.getAccountById(id);
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
            AccountDto accountDto = accountService.deposit(id,amount);
            return ResponseEntity.ok(new ApiResponse("Success!",accountDto));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<ApiResponse> withdraw(@PathVariable Long id,@RequestBody Map<String,Double> request){
        try {
            Double amount = request.get("amount");
            AccountDto accountDto = accountService.withdraw(id,amount);
            return ResponseEntity.ok(new ApiResponse("success!",accountDto));
        } catch (NotEnoughMoney | ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long id){
        try {
            accountService.deleteAccountById(id);
            return ResponseEntity.ok(new ApiResponse("Found",null));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
