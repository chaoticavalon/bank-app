package com.intevalue.bankingapi.exchangerate.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intevalue.bankingapi.exchangerate.ExchangeException;
import com.intevalue.bankingapi.exchangerate.FetchForex;
import com.intevalue.bankingapi.model.ExchangeRate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractFetchForex implements FetchForex {
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    protected abstract ResteasyWebTarget buildRequest(String to, String list);
    protected abstract Collection<ExchangeRate> read(String json) throws IOException;

    public static void main(String[] args) throws IOException, ExchangeException {
        FetchEuropeanForexImpl t = new FetchEuropeanForexImpl();
        t.get("PHP", Arrays.asList("USD,GBP,EUR"));

    }

    public Collection<ExchangeRate> get(String to, Collection<String> from) throws ExchangeException {
        try {
            return read(fetch(to, from.toArray(new String[0])));
        } catch (IOException e) {
            throw new ExchangeException(e);
        }
    }

    private String fetch(String to, Collection<String> symbols) throws ExchangeException {
        return fetch(to, symbols.iterator());
    }

    private String fetch(String to, Iterator<String> symbols) throws ExchangeException {
        Collection<String> iterable = IteratorUtils.toList(symbols);
        log.debug("Fetching symbols {}", iterable);
        String list = StringUtils.join(iterable, ",");

        String json = null;
        ResteasyWebTarget client = buildRequest(to, list);
        Response response = client.request(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE).get();
        json = response.readEntity(String.class);

        if (json == null) {
            throw new ExchangeException("Unable to get JSON from Yahoo API");
        }

        log.debug("Successfully requested symbols from Yahoo Exchange YQL API");
        log.trace("Response is {}", json);

        return json;
    }

    private String fetch(final String to, final String... from) throws ExchangeException {

        Iterator<String> iterator = IteratorUtils.arrayIterator(from);
        Collection<String> symbols = CollectionUtils.collect(iterator, input -> to + "," + input);

        return fetch(to, symbols);
    }

    protected void process(Set<ExchangeRate> exchangerates, String symbol, String value, String updatedDate) {

        if ("".equalsIgnoreCase(value)) {
            log.debug("The rate for {} has not been found", symbol);
        } else {
            exchangerates.add(new ExchangeRate(symbol, value,
                    DateTime.parse(updatedDate, DateTimeFormat.forPattern("yyyy-MM-dd")).toDate()));
        }
    }
}
