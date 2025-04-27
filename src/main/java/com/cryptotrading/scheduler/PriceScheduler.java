package com.cryptotrading.scheduler;

import com.cryptotrading.service.PriceAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private final PriceAggregatorService priceAggregatorService;

    @Scheduled(fixedRate = 10000)
    public void schedulePriceFetching() {
        priceAggregatorService.fetchAndStoreBestPrices();
    }
}

