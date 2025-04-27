package com.cryptotrading.model.dto;

import lombok.Data;

@Data
public class HoubiTickerDto {
    private String symbol;
    private double bid;
    private double ask;
}
