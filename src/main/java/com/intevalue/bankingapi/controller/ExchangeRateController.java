package com.intevalue.bankingapi.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intevalue.bankingapi.exchangerate.ExchangeRateService;
import com.intevalue.bankingapi.model.ExchangeRate;
import com.intevalue.bankingapi.model.MessageResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ExchangeRateController {
    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping("/api/exchangerate/refresh/{baseCurrency}")
    public ResponseEntity<MessageResponse<?>> refreshExchangeRate(@PathVariable("baseCurrency") String baseCurrency) {
        exchangeRateService.updateExchangeRate(baseCurrency);

        MessageResponse<?> messageResponse = MessageResponse.builder()
                .message("Exchange rate was successfully refreshed.")
                .status(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(messageResponse);
    }

    @RequestMapping("/api/exchangerate")
    public ResponseEntity<MessageResponse<?>> getExchangeRate(
            @RequestParam(name = "fromCurrency") String fromCurrency,
            @RequestParam(name = "toCurrency", required = false) List<String> toCurrency) {
        List<ExchangeRate> exchangeRates;
        if (!CollectionUtils.isEmpty(toCurrency)) {
            exchangeRates = exchangeRateService.findByToAndFrom(fromCurrency, toCurrency);
        } else {
            exchangeRates = exchangeRateService.findByFromCurrency(fromCurrency);
        }

        if (!exchangeRates.isEmpty()) {
            MessageResponse<?> messageResponse = MessageResponse.builder()
                    .status(HttpStatus.OK.value())
                    .data(exchangeRates)
                    .isSuccessful(true)
                    .message("Successful")
                    .build();
            return ResponseEntity.ok().body(messageResponse);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

}
