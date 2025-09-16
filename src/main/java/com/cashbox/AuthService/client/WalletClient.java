package com.cashbox.AuthService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cashbox.AuthService.dto.request.WalletCreateRequest;

@FeignClient(name = "wallet-service", url = "${wallet.service.url}")
public interface WalletClient {

    @PostMapping("/api/v1/wallets/create")
    void createWallet(@RequestBody WalletCreateRequest request);
}
