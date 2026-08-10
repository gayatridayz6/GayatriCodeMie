package com.library.config;

import com.library.model.CurrencyCode;
import com.library.repository.CurrencyCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CurrencyCodeDataLoader implements CommandLineRunner {

    private final CurrencyCodeRepository currencyCodeRepository;

    public CurrencyCodeDataLoader(CurrencyCodeRepository currencyCodeRepository) {
        this.currencyCodeRepository = currencyCodeRepository;
    }

    @Override
    public void run(String... args) {
        if (currencyCodeRepository.count() > 0) {
            return;
        }

        LocalDateTime sourceLastUpdated = LocalDateTime.now();
        currencyCodeRepository.save(new CurrencyCode("USD", 840, "United States dollar", sourceLastUpdated));
        currencyCodeRepository.save(new CurrencyCode("EUR", 978, "Euro", sourceLastUpdated));
        currencyCodeRepository.save(new CurrencyCode("GBP", 826, "Pound sterling", sourceLastUpdated));
    }
}
