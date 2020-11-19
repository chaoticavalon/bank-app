package com.intevalue.bankingapi.exchangerate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import com.intevalue.bankingapi.exchangerate.impl.FetchEuropeanForexImpl;
import com.intevalue.bankingapi.model.ExchangeRate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExchangeQuery {
    private Collection<String> from = new ArrayList<>();

    private String to;

    public ExchangeQuery from(String... symbols) {
        from = new ArrayList<>(Arrays.asList(symbols));
        return this;
    }

    public ExchangeQuery from(Collection<String> symbols) {
        from = symbols;
        return this;
    }

    public ExchangeQuery to(String symbol) {
        to = symbol;
        return this;
    }

    public Collection<ExchangeRate> get() {
        Collection<ExchangeRate> rates = null;
        Collection<String> tmp1 = new HashSet<>(from);

        try {
            FetchForex fetchForex = new FetchEuropeanForexImpl();
            rates = fetchForex.get(to, tmp1);
        } catch (ExchangeException e) {
            log.error("Unable to get exchange data from Yahoo", e);
        }

        return rates;
    }
}
