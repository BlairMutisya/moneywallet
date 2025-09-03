//package com.cashbox.moneywallet.authservice.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.Instant;
//
//@Entity
//@Table(name = "user_devices")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserDevice {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String deviceId;     // unique identifier from mobile
//    private String deviceName;   // e.g. "Samsung A54"
//    private String osType;       // Android / iOS
//    private String osVersion;
//    private String appVersion;
////    private String fcmToken;
//
//    private boolean isTrusted = false;
//    private Instant lastLoginAt;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//}