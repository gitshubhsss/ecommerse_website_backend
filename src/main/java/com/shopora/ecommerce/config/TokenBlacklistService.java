package com.shopora.ecommerce.config;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    // token → expiry time
    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    /**
     * Blacklist a token for 15 minutes
     */
    public void blacklistToken(String token) {
        long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000); // 15 minutes
        blacklist.put(token, expiryTime);
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isTokenBlacklisted(String token) {
        Long expiryTime = blacklist.get(token);

        if (expiryTime == null) {
            return false;
        }

        // Token expired → remove from blacklist
        if (System.currentTimeMillis() > expiryTime) {
            blacklist.remove(token);
            return false;
        }

        return true;
    }
}