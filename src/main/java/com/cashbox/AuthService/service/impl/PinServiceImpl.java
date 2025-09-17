package com.cashbox.AuthService.service.impl;

import com.cashbox.AuthService.dto.request.WalletCreateRequest;
import com.cashbox.AuthService.dto.response.PinCreateResponse;
import com.cashbox.AuthService.entity.WalletPin;
import com.cashbox.AuthService.enums.PinErrorCode;
import com.cashbox.AuthService.exception.PinException;
import com.cashbox.AuthService.repository.WalletPinRepository;
import com.cashbox.AuthService.service.AuthService;
import com.cashbox.AuthService.service.PinService;
import com.cashbox.AuthService.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cashbox.AuthService.client.WalletClient;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PinServiceImpl implements PinService {

    private final WalletPinRepository walletPinRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletClient walletClient;
    private final AuthUtils authUtils;

    // After this many failed attempts, PIN gets locked
    private static final int MAX_FAILED_ATTEMPTS = 3;

    // Lock duration in minutes after max attempts
    private static final int LOCK_DURATION_MINUTES = 15;

    @Override
    @Transactional
    public PinCreateResponse setPin(Long userId, String pin) {
        WalletPin walletPin = walletPinRepository.findByUserId(userId)
                .orElse(new WalletPin());

        walletPin.setUserId(userId);
        walletPin.setPinHash(passwordEncoder.encode(pin));
        walletPin.setFailedAttempts(0);
        walletPin.setLockedUntil(null);

        walletPinRepository.save(walletPin);
        log.info("PIN set/updated for user {}", userId);

        // call wallet service
        try {
            walletClient.createWallet(new WalletCreateRequest(userId));
            log.info("Wallet created successfully for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to create wallet for user {} after PIN set", userId, e);
        }
        return new PinCreateResponse(userId);
    }

    @Override
    @Transactional
    public void changePin(Long userId, String oldPin, String newPin) {
        WalletPin walletPin = walletPinRepository.findByUserId(userId)
                .orElseThrow(() -> new PinException(PinErrorCode.INVALID, "No PIN found for user"));

        // Check lock state
        if (walletPin.getLockedUntil() != null && walletPin.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new PinException(PinErrorCode.LOCKED, "PIN locked. Try again later");
        }

        // Validate old PIN
        if (!passwordEncoder.matches(oldPin, walletPin.getPinHash())) {
            handleFailedAttempt(walletPin);
            throw new PinException(PinErrorCode.INVALID, "Old PIN is incorrect");
        }

        // All good → set new PIN, reset attempts
        walletPin.setPinHash(passwordEncoder.encode(newPin));
        walletPin.setFailedAttempts(0);
        walletPin.setLockedUntil(null);
        walletPinRepository.save(walletPin);

        log.info(" PIN changed for user {}", userId);
    }

    @Override
    @Transactional
    public boolean verifyPin(String pin) {
        var currentUser = authUtils.getCurrentUser();
        Long userId = currentUser.getId();
        log.debug("Verifying PIN for user {}", userId);

        WalletPin walletPin = walletPinRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("No PIN found for user {}", userId);
                    return new PinException(PinErrorCode.INVALID, "No PIN found for user");
                });

        if (walletPin.getLockedUntil() != null && walletPin.getLockedUntil().isAfter(LocalDateTime.now())) {
            log.warn("User {} attempted PIN but is locked until {}", userId, walletPin.getLockedUntil());
            throw new PinException(PinErrorCode.LOCKED, "PIN locked. Try again later");
        }

        if (!passwordEncoder.matches(pin, walletPin.getPinHash())) {
            log.warn("Invalid PIN attempt for user {}", userId);
            handleFailedAttempt(walletPin);
            return false;
        }

        // Success
        log.debug("PIN verified successfully for user {}", userId);
        walletPin.setFailedAttempts(0);
        walletPin.setLockedUntil(null);
        walletPinRepository.save(walletPin);
        return true;
    }




    /**
     * Handles a failed PIN attempt: increment counter and lock if necessary.
     */
    private void handleFailedAttempt(WalletPin walletPin) {
        int attempts = walletPin.getFailedAttempts() + 1;
        walletPin.setFailedAttempts(attempts);

        if (attempts >= MAX_FAILED_ATTEMPTS) {
            walletPin.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            walletPin.setFailedAttempts(0); // reset after lock
            log.warn(" PIN locked for user {} until {}", walletPin.getUserId(), walletPin.getLockedUntil());
        }
        walletPinRepository.save(walletPin);
    }
}

