package fr.uge.confroid.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {
    private static final int IV_SIZE = 16;
    private static final byte[] IV = { 42, 89, 105, 1, 0, 125, 127, 100, 70, 12, 96, 25, 12, 0, 9, 124 };
    private static final int HASH_ITERATIONS = 2048;
    private static final int KEY_LENGTH = 256;

    /**
     * Encode the given string using the SHA-256 algorithm.
     *
     * @param input The string to encode
     * @return The encoded string
     */
    public static String hash(String input) {
        return Hashing.sha256().hashString(input, StandardCharsets.UTF_8).toString();
    }

    /**
     * Generates a secret key (symmetric) from password and salt using PBKDF2 function.
     *
     * @param password The password
     * @param salt The salt
     * @return The secret key
     */
    public static SecretKey getKeyFromPassword(String password, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), HASH_ITERATIONS, KEY_LENGTH);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            return secret;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Error while generating secret key : " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates an initialization vector (IV). It's a pseudo-random value and has
     * the same size as the block that is encrypted ({@value #IV_SIZE}).
     *
     * @return A new initialization vector
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);

        return new IvParameterSpec(iv);
    }

    /**
     * Encrypt a string using the AES algorithm with the given key and a optional
     * initialization vector.
     * If the initialization vector is not given, a default (and constant) one is used.
     *
     * @param input The string to encrypt
     * @param key The secret key
     * @param iv The initialization vector
     * @return The encoded String in base 64
     */
    public static String encrypt(String input, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            System.err.println("Error while encrypting the string : " + e.getMessage());
            return null;
        }
    }

    /**
     * Encrypt a string using the AES algorithm with the given key and a optional
     * initialization vector.
     * If the initialization vector is not given, a default (and constant) one is used.
     *
     * @param input The string to encrypt
     * @param key The secret key
     * @return The encoded String in base 64
     */
    public static String encrypt(String input, SecretKey key) {
        return encrypt(input, key, new IvParameterSpec(IV));
    }

    /**
     * Decrypt a string using the AES algorithm with the given key and a optional
     * initialization vector.
     * If the initialization vector is not given, a default (and constant) one is used.
     *
     * @param cipherText The string to decrypt in base 64
     * @param key The secret key
     * @param iv The initialization vector
     * @return The decoded String
     */
    public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

            return new String(decryptedText);
        } catch (Exception e) {
            System.err.println("Error while decrypting the string : " + e.getMessage());
            return null;
        }
    }

    /**
     * Decrypt a string using the AES algorithm with the given key and a optional
     * initialization vector.
     * If the initialization vector is not given, a default (and constant) one is used.
     *
     * @param cipherText The string to decrypt in base 64
     * @param key The secret key
     * @return The decoded String
     */
    public static String decrypt(String cipherText, SecretKey key) {
        return decrypt(cipherText, key, new IvParameterSpec(IV));
    }
}
