package com.intevalue.bankingapi.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intevalue.bankingapi.dto.UserAccountDTO;
import com.intevalue.bankingapi.model.Account;
import com.intevalue.bankingapi.model.MessageResponse;
import com.intevalue.bankingapi.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Account>> createUserAccount(@Valid @RequestBody UserAccountDTO userAccountDTO) throws Exception {
        MessageResponse<Account> messageResponse = null;
        Optional<Account> account = accountService.createAccount(userAccountDTO);
        if (account.isPresent()) {
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Account created successfully");
            messageResponse.setData(account.get());
            messageResponse.setStatus(HttpStatus.CREATED.value());
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/deactivate")
    public ResponseEntity<MessageResponse<Account>> closeUserAccount(@PathVariable Long id) {
        MessageResponse<Account> messageResponse = null;
        Optional<Account> optionalAccount = accountService.closeAccountById(id);
        if (optionalAccount.isPresent()) {
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Account closed successfully");
            messageResponse.setData(optionalAccount.get());
            messageResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseEntity.status(messageResponse.getStatus()).body(messageResponse);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<Account>> getUserAccount(@RequestParam @NotNull String accountNumber) {
        MessageResponse<Account> messageResponse = null;
        Optional<Account> optionalAccount = accountService.getAccountByAccountNumber(accountNumber);
        if (optionalAccount.isPresent()) {
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Successful");
            messageResponse.setData(optionalAccount.get());
            messageResponse.setStatus(HttpStatus.OK.value());
            return ResponseEntity.status(messageResponse.getStatus()).body(messageResponse);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<MessageResponse<List<Account>>> getAllUserAccount() {
        MessageResponse<List<Account>> messageResponse = null;
        List<Account> accounts = accountService.findAll();
        if (!accounts.isEmpty()) {
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Successful");
            messageResponse.setData(accounts);
            messageResponse.setStatus(HttpStatus.OK.value());
            return ResponseEntity.status(messageResponse.getStatus()).body(messageResponse);
        }
        return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);
    }
}
