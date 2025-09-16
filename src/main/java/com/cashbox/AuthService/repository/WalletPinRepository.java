package com.cashbox.AuthService.repository;

import com.cashbox.AuthService.entity.WalletPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletPinRepository extends JpaRepository<WalletPin, Long> {
    Optional<WalletPin> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}