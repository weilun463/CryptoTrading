package com.cryptotrading.controller;

import com.cryptotrading.model.entity.CryptoPriceEntity;
import com.cryptotrading.model.entity.TradeTransactionEntity;
import com.cryptotrading.model.entity.UserWalletEntity;
import com.cryptotrading.repository.CryptoPriceRepository;
import com.cryptotrading.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CryptoTradingController {

    @Autowired
    CryptoPriceRepository cryptoPriceRepository;
    @Autowired
    TradingService tradingService;

    //Get latest price
    @GetMapping("/prices/{symbol}")
    public CryptoPriceEntity getLatestPrice(@PathVariable String symbol) {
        return cryptoPriceRepository.findById(symbol.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Symbol not supported: " + symbol));
    }

    //Place a trade
    @PostMapping("/trade")
    public String trade(@RequestParam String symbol,
                        @RequestParam String tradeType,
                        @RequestParam BigDecimal quantity) {
        return tradingService.trade(symbol.toUpperCase(), tradeType.toUpperCase(), quantity);
    }

    //Get wallet balance
    @GetMapping("/wallet")
    public UserWalletEntity getWalletBalance() {
        return tradingService.getWallet();
    }

    //Get trade history
    @GetMapping("/history")
    public List<TradeTransactionEntity> getTradeHistory() {
        return tradingService.getTradeHistory();
    }
}
