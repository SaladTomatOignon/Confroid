package fr.uge.confroid.web.service.confroidstorageservice.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class CryptUtils {

    /**
     * Encode the given string using the SHA-256 algorithm.
     *
     * @param input The string to encode
     * @return The encoded string
     */
    public static String hash(String input) {
        return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
    }
}
