package com.cryptotrading.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TradeTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private String tradeType;
    private Double price;
    private Double quantity;
    private Double amount;
    private LocalDateTime tradeTime;
}
