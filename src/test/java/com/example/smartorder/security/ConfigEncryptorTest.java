package com.example.smartorder.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConfigEncryptorTest {
    @Value("${jasypt.encryptor.password}")
    private String password;

    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;

    @Test
    void stringEncryptor() {
        // Given
        String plainText = "password";

        // When
        String result = encryptText(plainText);

        // Then
        System.out.println(result);
    }

    private String encryptText(String plainText) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setPassword(this.password);
        pbeEnc.setAlgorithm(this.algorithm);

        return pbeEnc.encrypt(plainText);
    }
}