package com.cashbox.moneywallet.repository;

import com.cashbox.moneywallet.entity.RefreshToken;
import com.cashbox.moneywallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteAllByUser(User user);
}