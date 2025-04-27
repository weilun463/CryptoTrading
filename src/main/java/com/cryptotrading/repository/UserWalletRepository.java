package com.cryptotrading.repository;

import com.cryptotrading.model.entity.UserWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWalletRepository extends JpaRepository<UserWalletEntity, Long> {
}
