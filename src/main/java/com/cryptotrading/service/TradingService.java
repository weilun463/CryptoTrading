package com.cryptotrading.service;

import com.cryptotrading.model.entity.CryptoPriceEntity;
import com.cryptotrading.model.entity.TradeTransactionEntity;
import com.cryptotrading.model.entity.UserWalletEntity;
import com.cryptotrading.repository.CryptoPriceRepository;
import com.cryptotrading.repository.TradeTransactionRepository;
import com.cryptotrading.repository.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserWalletRepository userWalletRepository;
    private final CryptoPriceRepository cryptoPriceRepository;
    private final TradeTransactionRepository tradeTransactionRepository;

    @Transactional
    public String trade(String symbol, String tradeType, BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trade quantity must be greater than 0.");
        }

        UserWalletEntity wallet = userWalletRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));

        CryptoPriceEntity price = cryptoPriceRepository.findById(symbol)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price not found for symbol: " + symbol));

        BigDecimal executedPrice = BigDecimal.valueOf(
                tradeType.equalsIgnoreCase("BUY") ? price.getAskPrice() : price.getBidPrice()
        );

        BigDecimal amount = quantity.multiply(executedPrice);

        if (tradeType.equalsIgnoreCase("BUY")) {
            if (wallet.getUsdtBalance().compareTo(amount) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough USDT balance.");
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance().subtract(amount));

            if (symbol.equalsIgnoreCase("BTCUSDT")) {
                wallet.setBtcBalance(wallet.getBtcBalance().add(quantity));
            } else if (symbol.equalsIgnoreCase("ETHUSDT")) {
                wallet.setEthBalance(wallet.getEthBalance().add(quantity));
            }
        } else if (tradeType.equalsIgnoreCase("SELL")) {
            if (symbol.equalsIgnoreCase("BTCUSDT") && wallet.getBtcBalance().compareTo(quantity) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough BTC to sell.");
            } else if (symbol.equalsIgnoreCase("ETHUSDT") && wallet.getEthBalance().compareTo(quantity) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough ETH to sell.");
            }

            if (symbol.equalsIgnoreCase("BTCUSDT")) {
                wallet.setBtcBalance(wallet.getBtcBalance().subtract(quantity));
            } else if (symbol.equalsIgnoreCase("ETHUSDT")) {
                wallet.setEthBalance(wallet.getEthBalance().subtract(quantity));
            }
            wallet.setUsdtBalance(wallet.getUsdtBalance().add(amount));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid trade type. Use BUY or SELL.");
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
        return userWalletRepository.findById(1L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found"));
    }

    public List<TradeTransactionEntity> getTradeHistory() {
        return tradeTransactionRepository.findAllByOrderByTradeTimeDesc();
    }
}
