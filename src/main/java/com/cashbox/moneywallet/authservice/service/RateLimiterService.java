package com.cashbox.moneywallet.authservice.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Allow 10 requests per minute per IP
    private final int LIMIT = 10;
    private final Duration DURATION = Duration.ofMinutes(1);

    public boolean allowRequest(String ipAddress) {
        Bucket bucket = buckets.computeIfAbsent(ipAddress, key ->
                Bucket.builder()
                        .addLimit(Bandwidth.classic(LIMIT, Refill.intervally(LIMIT, DURATION)))
                        .build()
        );
        return bucket.tryConsume(1);
    }
}