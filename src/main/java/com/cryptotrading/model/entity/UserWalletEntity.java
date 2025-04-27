package com.cryptotrading.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class UserWalletEntity {

    @Id
    private Long id = 1L;
    private BigDecimal usdtBalance;
    private BigDecimal btcBalance;
    private BigDecimal ethBalance;
}
