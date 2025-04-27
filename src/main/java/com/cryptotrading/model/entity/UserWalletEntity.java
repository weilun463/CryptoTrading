package com.cryptotrading.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserWalletEntity {

    @Id
    private Long id = 1L;
    private Double usdtBalance = 50000.0;
    private Double btcBalance = 0.0;
    private Double ethBalance = 0.0;
}
