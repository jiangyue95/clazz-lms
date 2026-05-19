package com.yue.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Hashing utilities for non-password values.
 *
 * <p>Specifically for high-entropy random values like refresh tokens, where
 * SHA-256 is the correct choice - fast, deterministic, one-way. This is
 * deliberately NOT for passwords: passwords are low-entropy and human-chosen,
 * so they need BCrypt's deliberate slowness and per-hash salt. Refresh tokens
 * are server-generated random strings; their entropy comes from the generator,
 * not from a slow hash.
 *
 * <p>The hash is used so that a database/Redis compromise doesn't immediately
 * leak usable tokens - an attacker would see only hashes, not the raw tokens
 * needed to authenticate.
 */
public final class HashUtil {

    private HashUtil() {
        // utility class - no instances
    }

    /**
     * Compute the SHA-256 hash of the input, returned as a lowercase hex string.
     *
     * @param raw the input string (e.g. a raw refresh token)
     * @return 64-character lowercase hex representation of the SHA-256 digest
     */
    public static String sha256Hex(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed present in every JVM - this branch is
            // effectively unreachable, but the checked exception must be handled.
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
