package com.intevalue.bankingapi.service.impl;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.collect.Lists;
import com.intevalue.bankingapi.dto.UserAccountDTO;
import com.intevalue.bankingapi.model.Account;
import com.intevalue.bankingapi.model.Status;
import com.intevalue.bankingapi.model.User;
import com.intevalue.bankingapi.model.UserRole;
import com.intevalue.bankingapi.repository.AccountRepository;
import com.intevalue.bankingapi.repository.UserRepository;
import com.intevalue.bankingapi.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    private String generateAccountNumber() {
        char[] VALID_CHARACTERS = "0123456789".toCharArray();
        String accountNumber = null;
        SecureRandom secureRandom = new SecureRandom();
        char[] buff = new char[10];
        Random rand = new Random();

        boolean check = true;
        while (check) {
            for (int i = 0; i < 10; ++i) {
                if ((i % 9) == 0) {
                    rand.setSeed(secureRandom.nextLong());
                }
                buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
            }
            accountNumber = new String(buff);
            Optional<Account> account = accountRepository.getAccountByAccountNumber(accountNumber);
            if (!account.isPresent()) {
                break;
            }
        }
        return accountNumber;
    }

    public Optional<Account> createAccount(UserAccountDTO userAccountDTO) {
        Optional<User> userOptional = userRepository.getUserByEmail(userAccountDTO.getEmail());
        User user;
        if (!userOptional.isPresent()) {
            user = User.builder()
                    .email(userAccountDTO.getEmail())
                    .fullName(userAccountDTO.getFullName())
                    .role(UserRole.USER)
                    .phoneNumber(userAccountDTO.getPhoneNumber())
                    .address(userAccountDTO.getAddress())
                    .build();
            userRepository.save(user);
        } else {
            user = userOptional.get();
        }

        DateTime createdTime = DateTime.now();
        Account account = Account.builder()
                .user(user)
                .status(Status.ACTIVE)
                .dateTimeCreated(createdTime.toDate())
                .lastUpdateDateTime(createdTime.toDate())
                .availableBalance(userAccountDTO.getDeposit())
                .totalBalance(userAccountDTO.getDeposit())
                .currency(userAccountDTO.getCurrency())
                .accountNumber(generateAccountNumber())
                .build();

        accountRepository.save(account);

        return Optional.ofNullable(account);
    }

    public Optional<Account> closeAccountById(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setStatus(Status.CLOSED);
            account.setLastUpdateDateTime(DateTime.now().toDate());
            accountRepository.save(account);
        }
        return accountOptional;

    }

    public Optional<Account> closeAccount(String accountNumber) {
        Optional<Account> accountOptional = accountRepository.getAccountByAccountNumber(accountNumber);
        if (accountOptional.isPresent()) {
            return closeAccountById(accountOptional.get().getId());
        }
        return Optional.empty();
    }

    public Optional<Account> closeAccount(Account account) {
        return closeAccountById(account.getId());
    }

    public Optional<Account> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.getAccountByAccountNumber(accountNumber);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Optional<Account> updateAccount(Account account) {
        Optional<Account> optionalAccount = getAccountById(account.getId());
        if (!optionalAccount.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User account does not exist!");
        }

        Account accountFromDB = optionalAccount.get();
        account.setLastUpdateDateTime(account.getLastUpdateDateTime());
        BeanUtils.copyProperties(account, accountFromDB, "id");
        return Optional.ofNullable(accountRepository.save(accountFromDB));
    }

    @Override
    public List<Account> findAll() {
        return Lists.newArrayList(accountRepository.findAll());
    }
}
