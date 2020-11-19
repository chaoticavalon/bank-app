package com.intevalue.bankingapi.model;

import java.io.Serializable;
import java.util.Date;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity = User.class)
    private User user;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Double availableBalance;
    private Double totalBalance;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeCreated;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdateDateTime;
    private String currency;

}
