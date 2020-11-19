package com.intevalue.bankingapi.service.impl;

import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.intevalue.bankingapi.dto.DepositWithdrawDTO;
import com.intevalue.bankingapi.dto.FundTransferDTO;
import com.intevalue.bankingapi.model.Account;
import com.intevalue.bankingapi.model.Status;
import com.intevalue.bankingapi.model.Transaction;
import com.intevalue.bankingapi.model.TransactionType;
import com.intevalue.bankingapi.repository.TransactionRepository;
import com.intevalue.bankingapi.service.AccountService;
import com.intevalue.bankingapi.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Optional<Transaction> deposit(DepositWithdrawDTO depositWithdrawDTO) {
        Optional<Account> optionalAccount = accountService.getAccountByAccountNumber(depositWithdrawDTO.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        } else {
            Account account = optionalAccount.get();
            if (account.getStatus() == Status.CLOSED) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account has been closed!");
            } else if (!account.getCurrency().equals(depositWithdrawDTO.getCurrency())) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Deposit is only allowed on same currency");
            }
            DateTime currentDateTime = DateTime.now();
            Transaction transaction = Transaction.builder()
                    .depositWithdrawalAccount(account)
                    .transactionType(TransactionType.DEPOSIT)
                    .amount(depositWithdrawDTO.getAmount())
                    .currency(depositWithdrawDTO.getCurrency())
                    .transactionDateTime(currentDateTime.toDate())
                    .build();
            transactionRepository.save(transaction);

            account.setAvailableBalance(account.getTotalBalance() + depositWithdrawDTO.getAmount());
            account.setTotalBalance(account.getTotalBalance() + depositWithdrawDTO.getAmount());
            account.setLastUpdateDateTime(currentDateTime.toDate());
            accountService.updateAccount(account);

            return Optional.of(transaction);
        }
    }

    @Override
    public Optional<Transaction> transfer(FundTransferDTO fundTransferDTO) {
        Optional<Account> optionalSourceAccount = accountService.getAccountByAccountNumber(fundTransferDTO.getAccountTransferFrom());
        Optional<Account> optionalDestinationAccount = accountService.getAccountByAccountNumber(fundTransferDTO.getAccountTransferTo());

        validateTransferFund(fundTransferDTO, optionalSourceAccount,
                optionalDestinationAccount);


        Account senderAccount = optionalSourceAccount.get();
        Account recipientAccount = optionalDestinationAccount.get();

        recipientAccount.setAvailableBalance(recipientAccount.getAvailableBalance() + fundTransferDTO.getAmount());
        recipientAccount.setTotalBalance(recipientAccount.getTotalBalance() + fundTransferDTO.getAmount());
        accountService.updateAccount(recipientAccount);

        senderAccount.setTotalBalance(senderAccount.getTotalBalance() - fundTransferDTO.getAmount());
        senderAccount.setAvailableBalance(senderAccount.getAvailableBalance() - fundTransferDTO.getAmount());
        accountService.updateAccount(senderAccount);

        Transaction transaction = Transaction.builder()
            .accountTransferFrom(senderAccount)
            .accountTransferTo(recipientAccount)
            .transactionType(TransactionType.FUND_TRANSFER)
            .currency(fundTransferDTO.getCurrency())
            .amount(fundTransferDTO.getAmount())
            .transactionDateTime(DateTime.now().toDate())
            .build();

        transactionRepository.save(transaction);

        return Optional.ofNullable(transaction);
    }

    private void validateTransferFund(FundTransferDTO fundTransferDTO,
                                      Optional<Account> optionalSourceAccount, Optional<Account> optionalDestinationAccount) {
        if (!optionalSourceAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender account does not exist!");
        } else if (optionalSourceAccount.get().getStatus() == Status.CLOSED) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Sender account is already closed!");
        } else if (optionalSourceAccount.get().getAvailableBalance() < fundTransferDTO.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        } else if (!fundTransferDTO.getCurrency().equals(optionalSourceAccount.get().getCurrency())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Transfer is only allowed on same currency");
        }

        if (!optionalDestinationAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient account does not exist!");
        } else if (optionalDestinationAccount.get().getStatus() == Status.CLOSED) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Recipient account is already closed!");
        }

        if (fundTransferDTO.getAccountTransferFrom().equals(fundTransferDTO.getAccountTransferTo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot transfer funds to same account");
        }
    }

    @Override
    public List<Transaction> getAllTransactionByAccountNumber(String accountNumber) {
        Optional<Account> optionalAccount = accountService.getAccountByAccountNumber(accountNumber);
        if (!optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        }

        return transactionRepository.getAllByDepositWithdrawalAccountOrderByTransactionDateTimeAsc(optionalAccount.get().getId());
    }

    @Override
    public Optional<Transaction> withdraw(DepositWithdrawDTO depositWithdrawDTO) {
        Optional<Account> optionalAccount = accountService.getAccountByAccountNumber(depositWithdrawDTO.getAccountNumber());
        if (!optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist!");
        } else if (optionalAccount.get().getAvailableBalance() < depositWithdrawDTO.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance");
        } else if (!optionalAccount.get().getCurrency().equals(depositWithdrawDTO.getCurrency())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Withdrawal is only allowed on same currency");
        }

        Account account = optionalAccount.get();

        account.setTotalBalance(account.getTotalBalance() - depositWithdrawDTO.getAmount());
        account.setAvailableBalance(account.getAvailableBalance() - depositWithdrawDTO.getAmount());
        accountService.updateAccount(account);

        Transaction transaction = Transaction.builder()
                .depositWithdrawalAccount(account)
                .transactionType(TransactionType.WITHDRAW)
                .amount(depositWithdrawDTO.getAmount())
                .currency(depositWithdrawDTO.getCurrency())
                .transactionDateTime(DateTime.now().toDate())
                .build();

        transactionRepository.save(transaction);

        return Optional.ofNullable(transaction);
    }
}
