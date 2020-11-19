package com.intevalue.bankingapi.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exchange_rate")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_currency")
    private String fromCurrency;
    @Column(name = "to_currency")
    private String toCurrency;
    private String symbol;
    private String name;

    private BigDecimal rate;
    @Temporal(TemporalType.DATE)
    private Date updatedDate;

    public ExchangeRate(String forex, String rate, Date updatedDate) {
        this.toCurrency = forex.substring(0, 3);
        this.fromCurrency = forex.substring(3, 6);
        this.symbol = fromCurrency + toCurrency;
        this.name = fromCurrency + "/" + toCurrency;
        this.updatedDate = updatedDate;
        this.rate = new BigDecimal("1.0").divide(new BigDecimal(rate), 8, RoundingMode.HALF_EVEN);
    }

    public BigDecimal convert(int value) {
        return rate.multiply(new BigDecimal(value));
    }

    public BigDecimal convert(BigDecimal value) {
        return rate.multiply(value);
    }
}
