package com.cashbox.moneywallet.authservice.repository;

import com.cashbox.moneywallet.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}