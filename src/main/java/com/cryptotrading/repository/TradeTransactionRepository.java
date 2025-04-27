package com.cryptotrading.repository;

import com.cryptotrading.model.entity.TradeTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeTransactionRepository extends JpaRepository<TradeTransactionEntity, Long> {

    List<TradeTransactionEntity> findAllByOrderByTradeTimeDesc();
}
