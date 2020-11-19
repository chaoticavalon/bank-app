package com.intevalue.bankingapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.intevalue.bankingapi.model.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByToCurrency(String baseCurrency);
    Optional<ExchangeRate> findByToCurrencyAndFromCurrency(String to, String from);

    @Query( "select o from ExchangeRate o where fromCurrency = :fromCurrency and toCurrency in :toCurrency" )
    List<ExchangeRate> findByToCurrencyAndFromCurrencyIn(@Param("fromCurrency") String fromCurrency, @Param("toCurrency") List<String> toCurrency);

    List<ExchangeRate> findByFromCurrency(String fromCurrency);

}
