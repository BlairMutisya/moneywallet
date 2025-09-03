package com.cashbox.moneywallet.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    private String deviceId;
    private String deviceName;
    private String osType;
    private String osVersion;
    private String appVersion;
    private String fcmToken;
    private boolean isTrusted; // initially false, set true after successful login
}
