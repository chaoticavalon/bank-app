package com.intevalue.bankingapi.service;

import java.util.List;
import java.util.Optional;

import com.intevalue.bankingapi.dto.UserAccountDTO;
import com.intevalue.bankingapi.model.Account;


public interface AccountService {
    Optional<Account> createAccount(UserAccountDTO userAccountDTO);
    Optional<Account> closeAccount(String accountNumber);
    Optional<Account> getAccountByAccountNumber(String accountNumber);
    Optional<Account> getAccountById(Long id);
    Optional<Account> closeAccount(Account account);
    Optional<Account> closeAccountById(Long id);
    Optional<Account> updateAccount(Account account);
    List<Account> findAll();
}
