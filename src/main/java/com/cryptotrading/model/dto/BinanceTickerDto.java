package com.cryptotrading.model.dto;


import lombok.Data;

@Data
public class BinanceTickerDto {
    private String symbol;
    private String bidPrice;
    private String askPrice;
}
