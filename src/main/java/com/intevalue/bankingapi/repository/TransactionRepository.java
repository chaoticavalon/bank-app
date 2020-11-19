package com.intevalue.bankingapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.intevalue.bankingapi.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query( "select o from Transaction o where  o.accountTransferFrom.id = :id or o.depositWithdrawalAccount.id = :id order by o.transactionDateTime desc" )
    List<Transaction> getAllByDepositWithdrawalAccountOrderByTransactionDateTimeAsc(@Param("id") Long id);


}
