package com.boardroom.boardroom_booking.utils;

import java.security.SecureRandom;

public class SecretTokenGenerator {
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        StringBuilder sb = new StringBuilder("LP-");
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
