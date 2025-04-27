package com.cryptotrading.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class TradeTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private String tradeType;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal amount;
    private LocalDateTime tradeTime;
}
