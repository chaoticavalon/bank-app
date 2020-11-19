package com.intevalue.bankingapi.exchangerate;

import java.util.Collection;

import com.intevalue.bankingapi.model.ExchangeRate;

public interface FetchForex {

    Collection<ExchangeRate> get(String to, Collection<String> from) throws ExchangeException;

}
