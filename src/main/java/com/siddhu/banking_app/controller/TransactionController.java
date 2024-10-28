package com.siddhu.banking_app.controller;

import com.siddhu.banking_app.dto.TransactionCreateDto;
import com.siddhu.banking_app.dto.TransactionDto;
import com.siddhu.banking_app.dto.TransactionRequest;
import com.siddhu.banking_app.exceptions.AccountBlocked;
import com.siddhu.banking_app.exceptions.NotEnoughMoney;
import com.siddhu.banking_app.exceptions.ResourceNotFoundException;
import com.siddhu.banking_app.response.ApiResponse;
import com.siddhu.banking_app.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequestMapping("${api.prefix}/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createTransaction(@RequestBody TransactionCreateDto transactionCreateDto){
        try {
            transactionService.createTransaction(transactionCreateDto);
            return ResponseEntity.ok(new ApiResponse("Success!",null));
        } catch (ResourceNotFoundException | NotEnoughMoney | AccountBlocked e) {
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionDto>> getAllTransactions(@PathVariable Long accountId){
        try {
            List<TransactionDto> transactionDtos = transactionService.getAllTransitionsById(accountId);
            return ResponseEntity.ok(transactionDtos);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(null);
        }
    }

    @GetMapping("/by_account_number/transactions")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByAccountNumber(@RequestBody TransactionRequest accountNumber){
        try {
            List<TransactionDto> transactionDtos = transactionService.getAllTransitionsByAccountNumber(accountNumber);
            return ResponseEntity.ok(transactionDtos);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(NOT_FOUND).body(null);
        }
    }
}
