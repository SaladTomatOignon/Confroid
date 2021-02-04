package fr.uge.confroid.utils;

import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static org.junit.Assert.*;

public class CryptographyTest {

    @Test
    public void defaultIV_cryptAndDecryptTest() {
        String plainText = "https://github.com/SaladTomatOignon/Confroid";
        String password = "SaladeTomateOignon";
        String salt = "saltbae";

        SecretKey key = CryptUtils.getKeyFromPassword(password, salt);

        String cipherText = CryptUtils.encrypt(plainText, key);
        String decryptedCipherText = CryptUtils.decrypt(cipherText, key);

        assertEquals(plainText, decryptedCipherText);
    }

    @Test
    public void generatedIV_cryptAndDecryptTest() {
        String plainText = "https://github.com/SaladTomatOignon/Confroid";
        String password = "SaladeTomateOignon";
        String salt = "saltbae";

        IvParameterSpec ivParameterSpec = CryptUtils.generateIv();
        SecretKey key = CryptUtils.getKeyFromPassword(password, salt);

        String cipherText = CryptUtils.encrypt(plainText, key, ivParameterSpec);
        String decryptedCipherText = CryptUtils.decrypt(cipherText, key, ivParameterSpec);

        assertEquals(plainText, decryptedCipherText);
    }
}
