package com.intevalue.bankingapi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = Account.class)
    private Account accountTransferFrom;
    @ManyToOne(targetEntity = Account.class)
    private Account accountTransferTo;
    @ManyToOne(targetEntity = Account.class)
    private Account depositWithdrawalAccount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDateTime;
    private Double amount;
    @ManyToOne(targetEntity = User.class)
    private User createBy;
    private String currency;

}
