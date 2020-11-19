package com.intevalue.bankingapi.exchangerate.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.ws.rs.client.Client;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intevalue.bankingapi.exchangerate.ExchangeException;
import com.intevalue.bankingapi.model.ExchangeRate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FetchEuropeanForexImpl extends AbstractFetchForex {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ENDPOINT = "https://api.exchangeratesapi.io/latest?symbols={symbols}&base={base}";

    protected ResteasyWebTarget buildRequest(String to, String list) {
        Client client = ResteasyClientBuilder.newClient();

        ResteasyWebTarget target = (ResteasyWebTarget) client.target(
                ENDPOINT.replace("{symbols}", list)
                .replace("{base}", to));

        return target;
    }

    public static void main(String[] args) throws IOException, ExchangeException {
        FetchEuropeanForexImpl t = new FetchEuropeanForexImpl();
        t.get("PHP", Arrays.asList("USD,GBP,EUR"));

    }

    /**
     *         {
     *             "rates": {
     *                         "EUR": 0.0174249421,
     *                         "USD": 0.0207043162,
     *                         "GBP": 0.0156101343
     *                     },
     *             "base": "PHP",
     *             "date": "2020-11-17"
     *         }
     * @param json
     * @return
     * @throws IOException
     */
    protected Collection<ExchangeRate> read(String json) throws IOException {
        JsonNode root = MAPPER.readTree(json);
        if (root == null) {
            throw new IOException("Invalid JSON received: " + json);
        }

        LinkedHashMap<String, String> rates = MAPPER.convertValue(root.get("rates"), LinkedHashMap.class);
        if (rates == null) {
            throw new IOException("No rates element has been found in received JSON: " + json);
        }

        String base = root.get("base").asText();
        if (base == null) {
            throw new IOException("No base element has been found in received JSON: " + json);
        }

        String date = root.get("date").asText();
        if (date == null) {
            throw new IOException("No date element has been found in received JSON: " + json);
        }

        Set<ExchangeRate> exchangerates = new HashSet<>();

        if (!rates.isEmpty()) {
            rates.entrySet().stream().forEach(e -> process(exchangerates, e.getKey() + base,
                    String.valueOf(e.getValue()), date));

        }

        return exchangerates;
    }
}
