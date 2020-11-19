package com.intevalue.bankingapi.service;

import java.util.List;
import java.util.Optional;

import com.intevalue.bankingapi.dto.DepositWithdrawDTO;
import com.intevalue.bankingapi.dto.FundTransferDTO;
import com.intevalue.bankingapi.model.Transaction;

public interface TransactionService {
    Optional<Transaction> deposit(DepositWithdrawDTO depositWithdrawDTO);
    Optional<Transaction> withdraw(DepositWithdrawDTO depositWithdrawDTO);
    Optional<Transaction> transfer(FundTransferDTO fundTransferDTO);
    List<Transaction> getAllTransactionByAccountNumber(String accountNumber);

}
