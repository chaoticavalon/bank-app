package com.intevalue.bankingapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.intevalue.bankingapi.model.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> getAccountByAccountNumber(String accountNumber);
}
