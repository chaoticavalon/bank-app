package com.intevalue.bankingapi.exchangerate.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intevalue.bankingapi.exchangerate.ExchangeException;
import com.intevalue.bankingapi.exchangerate.FetchForex;
import com.intevalue.bankingapi.model.ExchangeRate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FixerIOForexImpl extends AbstractFetchForex {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ENDPOINT = "https://data.fixer.io/api/latest?symbols={symbols}&base={base}";
    private static final String API_ACCESS_KEY = "2fa13096efa5c431137e3e4abd5cc570";

    protected ResteasyWebTarget buildRequest(String to, String list) {
        Client client = ResteasyClientBuilder.newClient();

        ResteasyWebTarget target = (ResteasyWebTarget) client.target(
                ENDPOINT.replace("{symbols}", list)
                .replace("{base}", to) + "&access_key=" + API_ACCESS_KEY);

        return target;
    }

    protected Collection<ExchangeRate> read(String json) throws JsonProcessingException, IOException {

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
