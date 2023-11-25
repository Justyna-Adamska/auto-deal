package com.example.autodeal.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("abc"));
    }

    public static Boolean matches(String raw, String encoded) {
        return new BCryptPasswordEncoder().matches(raw, encoded);
    }

    public static String encode(String raw) {
        return new BCryptPasswordEncoder().encode(raw);
    }
}
