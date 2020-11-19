package com.intevalue.bankingapi.scheduler;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.intevalue.bankingapi.exchangerate.ExchangeRateService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExchangeRateScheduler {
    @Autowired
    private ExchangeRateService exchangeService;
    @Value("${exchangerate.default.base}")
    private String baseCurrency;

    private ExecutorServiceThreadPool executorServiceThreadPool;

    @Scheduled(cron = "${exchangerate.scheduler.cron}")
    public void updateExchangeRate() {
        String[] currency = Arrays.stream(baseCurrency.split(",")).map(String::trim).toArray(String[]::new);
        executorServiceThreadPool = new ExecutorServiceThreadPool();

        for (String cur : currency) {
            executorServiceThreadPool.addThread(new BaseCurrencyProducer(cur));
            executorServiceThreadPool.addThread(new BaseCurrencyConsumer());
        }

        executorServiceThreadPool.finish();
    }

    //@EventListener(ApplicationReadyEvent.class)
    public void updateExchangeRateOnStartUp() {
        updateExchangeRate();
    }

    private class ExecutorServiceThreadPool {
        final BlockingQueue<String> queue = new ArrayBlockingQueue<>(20);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        protected void addThread(Runnable r) {
            executor.submit(r);
        }

        protected void finish() {
            try {
                executor.shutdown();
                executor.awaitTermination(50, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                log.error(null, ex);
            }
            System.out.println("Finished all threads");
        }
    }

    private class BaseCurrencyProducer implements Runnable {
        private String baseCurrency;

        public BaseCurrencyProducer(String baseCurrency) {
            this.baseCurrency = baseCurrency;
        }
        @Override
        public void run() {
            try {
                executorServiceThreadPool.queue.put(baseCurrency);
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    private class BaseCurrencyConsumer implements Runnable {
        private String baseCurrency;

        @Override
        public void run() {
            try {
                baseCurrency = executorServiceThreadPool.queue.take();
                exchangeService.updateExchangeRate(baseCurrency);
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }


}
