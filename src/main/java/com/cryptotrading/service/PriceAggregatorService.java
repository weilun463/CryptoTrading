package com.cryptotrading.service;

import com.cryptotrading.model.dto.BinanceTickerDto;
import com.cryptotrading.model.dto.HoubiTickerResponseDto;
import com.cryptotrading.model.entity.CryptoPriceEntity;
import com.cryptotrading.repository.CryptoPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceAggregatorService {

    @Autowired
    CryptoPriceRepository cryptoPriceRepository;

    private final RestTemplate restTemplate = new RestTemplate();;

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String HUOBI_URL = "https://api.huobi.pro/market/tickers";
    private static final String[] SUPPORTED_SYMBOLS = {"BTCUSDT", "ETHUSDT"};

    public void fetchAndStoreBestPrices() {
        Map<String, PriceData> binancePrices = fetchBinancePrices();
        Map<String, PriceData> huobiPrices = fetchHuobiPrices();

        Arrays.stream(SUPPORTED_SYMBOLS).forEach(symbol -> {
            PriceData binance = binancePrices.get(symbol);
            PriceData huobi = huobiPrices.get(symbol);

            if (binance != null && huobi != null) {
                double bestBid = Math.max(binance.bid(), huobi.bid());
                double bestAsk = Math.min(binance.ask(), huobi.ask());

                CryptoPriceEntity price = new CryptoPriceEntity();
                price.setSymbol(symbol);
                price.setBidPrice(bestBid);
                price.setAskPrice(bestAsk);
                price.setUpdatedAt(Instant.now());

                cryptoPriceRepository.save(price);

                System.out.printf("Saved price for %s - Bid: %.2f, Ask: %.2f, Time: %s%n", symbol, bestBid, bestAsk, price.getUpdatedAt().toString());
            }
        });
    }

    private Map<String, PriceData> fetchBinancePrices() {
        BinanceTickerDto[] tickers = restTemplate.getForObject(BINANCE_URL, BinanceTickerDto[].class);
        if (tickers == null) {
            return Map.of();
        }

        return Arrays.stream(tickers)
                .filter(ticker -> Arrays.asList(SUPPORTED_SYMBOLS).contains(ticker.getSymbol()))
                .collect(Collectors.toMap(
                        BinanceTickerDto::getSymbol,
                        t -> new PriceData(Double.parseDouble(t.getBidPrice()), Double.parseDouble(t.getAskPrice()))
                ));
    }

    private Map<String, PriceData> fetchHuobiPrices() {
        HoubiTickerResponseDto response = restTemplate.getForObject(HUOBI_URL, HoubiTickerResponseDto.class);
        if (response == null || response.getData() == null) {
            return Map.of();
        }

        return Arrays.stream(response.getData())
                .filter(ticker -> Arrays.asList(SUPPORTED_SYMBOLS).contains(ticker.getSymbol().toUpperCase()))
                .collect(Collectors.toMap(
                        t -> t.getSymbol().toUpperCase(),
                        t -> new PriceData(t.getBid(), t.getAsk())
                ));
    }

    private record PriceData(double bid, double ask) {}
}
