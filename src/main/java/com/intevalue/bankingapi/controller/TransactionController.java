package com.intevalue.bankingapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.intevalue.bankingapi.dto.DepositWithdrawDTO;
import com.intevalue.bankingapi.dto.FundTransferDTO;
import com.intevalue.bankingapi.model.MessageResponse;
import com.intevalue.bankingapi.model.Transaction;
import com.intevalue.bankingapi.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, value = "/deposit")
    public ResponseEntity<MessageResponse<Transaction>> depositFund(@Valid @RequestBody DepositWithdrawDTO depositWithdrawDTO) {
        MessageResponse<Transaction> messageResponse;
        Optional<Transaction> transaction = transactionService.deposit(depositWithdrawDTO);
        messageResponse = new MessageResponse<>();
        messageResponse.setMessage("Successful");
        messageResponse.setData(transaction.get());
        messageResponse.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/withdraw")
    public ResponseEntity<MessageResponse<Transaction>> withdrawFund(@Valid @RequestBody DepositWithdrawDTO depositWithdrawDTO) {
        MessageResponse<Transaction> messageResponse;
        Optional<Transaction> transaction = transactionService.withdraw(depositWithdrawDTO);
        messageResponse = new MessageResponse<>();
        messageResponse.setMessage("Successful");
        messageResponse.setData(transaction.get());
        messageResponse.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transfer")
    public ResponseEntity<MessageResponse<Transaction>> transferFund(@Valid @RequestBody FundTransferDTO fundTransferDTO) {
        MessageResponse<Transaction> messageResponse;
        Optional<Transaction> transaction = transactionService.transfer(fundTransferDTO);
        messageResponse = new MessageResponse<>();
        messageResponse.setMessage("Successful");
        messageResponse.setData(transaction.get());
        messageResponse.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/history/{accountNumber}")
    public ResponseEntity<MessageResponse<List<Transaction>>> transactionHistory(@Valid @PathVariable String accountNumber) {
        MessageResponse<List<Transaction>> messageResponse;
        List<Transaction> transaction = transactionService.getAllTransactionByAccountNumber(accountNumber);
        messageResponse = new MessageResponse<>();
        messageResponse.setMessage("Successful");
        messageResponse.setData(transaction);
        messageResponse.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
}
