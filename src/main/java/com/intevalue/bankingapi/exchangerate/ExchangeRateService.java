package com.intevalue.bankingapi.exchangerate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.intevalue.bankingapi.model.ExchangeRate;
import com.intevalue.bankingapi.repository.ExchangeRateRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExchangeRateService {
    @Value("${exchangerate.default.base}")
    private String base;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    public void updateExchangeRate(String base) {
        Collection<ExchangeRate> rates = new ExchangeQuery()
                .to(base)
                .get();

        Collection<ExchangeRate> toBeUpdatedList = new ArrayList<>();
        for (ExchangeRate exchangeRate : rates) {
            Optional<ExchangeRate> exchangeRateOptional = exchangeRateRepository.findByToCurrencyAndFromCurrency(exchangeRate.getToCurrency(),
                    exchangeRate.getFromCurrency());
            if (exchangeRateOptional.isPresent()) {
                ExchangeRate toBeUpdated = exchangeRateOptional.get();
                toBeUpdated.setRate(exchangeRate.getRate());
                toBeUpdated.setUpdatedDate(exchangeRate.getUpdatedDate());
                toBeUpdatedList.add(toBeUpdated);
            } else {
                toBeUpdatedList.add(exchangeRate);
            }
        }
        if (!toBeUpdatedList.isEmpty()) {
            exchangeRateRepository.saveAll(toBeUpdatedList);
        }
    }

    public List<ExchangeRate> findByToAndFrom(String fromCurrency, List<String> toCurrency) {
        return exchangeRateRepository.findByToCurrencyAndFromCurrencyIn(fromCurrency, toCurrency);
    }

    public List<ExchangeRate> findByFromCurrency(String fromCurrency) {
        return exchangeRateRepository.findByFromCurrency(fromCurrency);
    }

}
