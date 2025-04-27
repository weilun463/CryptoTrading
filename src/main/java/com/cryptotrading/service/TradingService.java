package com.cryptotrading.service;

import com.cryptotrading.model.entity.CryptoPriceEntity;
import com.cryptotrading.model.entity.TradeTransactionEntity;
import com.cryptotrading.model.entity.UserWalletEntity;
import com.cryptotrading.repository.CryptoPriceRepository;
import com.cryptotrading.repository.TradeTransactionRepository;
import com.cryptotrading.repository.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradingService {

    @Autowired
    UserWalletRepository userWalletRepository;
    @Autowired
    CryptoPriceRepository cryptoPriceRepository;
    @Autowired
    TradeTransactionRepository tradeTransactionRepository;

    @Transactional
    public String trade(String symbol, String tradeType, Double quantity) {

        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trade quantity must be greater than 0.");
        }

        UserWalletEntity wallet = userWalletRepository.findById(1L).orElseThrow();
        CryptoPriceEntity price = cryptoPriceRepository.findById(symbol).orElseThrow();

        double executedPrice = tradeType.equalsIgnoreCase("BUY") ? price.getAskPrice() : price.getBidPrice();
        double amount = quantity * executedPrice;

        if (tradeType.equalsIgnoreCase("BUY")) {
            if (wallet.getUsdtBalance() < amount) {
                throw new RuntimeException("Not enough USDT balance.");
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() - amount);
            if (symbol.equalsIgnoreCase("BTCUSDT")) {
                wallet.setBtcBalance(wallet.getBtcBalance() + quantity);
            } else if (symbol.equalsIgnoreCase("ETHUSDT")) {
                wallet.setEthBalance(wallet.getEthBalance() + quantity);
            }
        } else if (tradeType.equalsIgnoreCase("SELL")) {
            if (symbol.equalsIgnoreCase("BTCUSDT") && wallet.getBtcBalance() < quantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough BTU to sell.");
            } else if (symbol.equalsIgnoreCase("ETHUSDT") && wallet.getEthBalance() < quantity) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough ETH to sell.");
            }

            if (symbol.equalsIgnoreCase("BTCUSDT")) {
                wallet.setBtcBalance(wallet.getBtcBalance() - quantity);
            } else if (symbol.equalsIgnoreCase("ETHUSDT")) {
                wallet.setEthBalance(wallet.getEthBalance() - quantity);
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance() + amount);
        } else {
            throw new RuntimeException("Invalid trade type. Use BUY or SELL.");
        }
        userWalletRepository.save(wallet);

        TradeTransactionEntity transaction = new TradeTransactionEntity();
        transaction.setSymbol(symbol);
        transaction.setTradeType(tradeType.toUpperCase());
        transaction.setPrice(executedPrice);
        transaction.setQuantity(quantity);
        transaction.setAmount(amount);
        transaction.setTradeTime(LocalDateTime.now());
        tradeTransactionRepository.save(transaction);

        return "Trade executed successfully.";
    }

    public UserWalletEntity getWallet() {
        return userWalletRepository.findById(1L).orElseThrow();
    }

    public List<TradeTransactionEntity> getTradeHistory() {
        return tradeTransactionRepository.findAllByOrderByTradeTimeDesc();
    }
}
