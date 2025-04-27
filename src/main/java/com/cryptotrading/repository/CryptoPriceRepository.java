package com.cryptotrading.repository;

import com.cryptotrading.model.entity.CryptoPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoPriceRepository extends JpaRepository<CryptoPriceEntity, String> {
}