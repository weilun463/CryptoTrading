package com.cryptotrading.config;

import com.cryptotrading.model.entity.UserWalletEntity;
import com.cryptotrading.repository.UserWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserWalletRepository userWalletRepository;
    @Override
    public void run(String... args) throws Exception {
        if (userWalletRepository.findById(1L).isEmpty()) {
            UserWalletEntity wallet = new UserWalletEntity();
            wallet.setId(1L);
            wallet.setUsdtBalance(50000.0);
            wallet.setBtcBalance(0.0);
            wallet.setEthBalance(0.0);

            userWalletRepository.save(wallet);

            System.out.println("Initial wallet created with 50,000 USDT");
        }
    }
}

