package com.cryptotrading.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class CryptoPriceEntity {

    @Id
    private String symbol;
    private Double bidPrice;
    private Double askPrice;
    private Instant updatedAt;
}
