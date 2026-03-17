package app.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 纯 JDK 实现的可迁移口令哈希：
 * 存储格式：sha256$<saltB64>$<hashB64>
 * hash = SHA-256(salt || passwordUtf8)
 */
public final class PasswordHasher {
    private static final SecureRandom RNG = new SecureRandom();

    private PasswordHasher() {}

    public static String hash(String password) {
        if (password == null) password = "";
        byte[] salt = new byte[16];
        RNG.nextBytes(salt);
        byte[] digest = sha256(concat(salt, password.getBytes(StandardCharsets.UTF_8)));
        return "sha256$" + b64(salt) + "$" + b64(digest);
    }

    public static boolean verify(String password, String stored) {
        if (stored == null) return false;
        if (password == null) password = "";
        try {
            String[] p = stored.split("\\$");
            if (p.length != 3) return false;
            if (!"sha256".equalsIgnoreCase(p[0])) return false;
            byte[] salt = Base64.getDecoder().decode(p[1]);
            byte[] expect = Base64.getDecoder().decode(p[2]);
            byte[] got = sha256(concat(salt, password.getBytes(StandardCharsets.UTF_8)));
            return constantTimeEquals(expect, got);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] sha256(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String b64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static byte[] concat(byte[] a, byte[] b) {
        byte[] out = new byte[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) return false;
        int diff = a.length ^ b.length;
        int len = Math.min(a.length, b.length);
        for (int i = 0; i < len; i++) diff |= (a[i] ^ b[i]);
        return diff == 0;
    }
}

