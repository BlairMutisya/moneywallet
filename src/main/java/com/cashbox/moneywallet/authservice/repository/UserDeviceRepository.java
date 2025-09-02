package com.cashbox.moneywallet.authservice.repository;

import com.cashbox.moneywallet.authservice.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    List<UserDevice> findByUserId(Long userId);
    Optional<UserDevice> findByDeviceId(String deviceId);
    void deleteByDeviceId(String deviceId);
}