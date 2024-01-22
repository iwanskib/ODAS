package odas.service;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class EncryptionService {
    private AES256TextEncryptor getTextEncryptor(String key) {
        AES256TextEncryptor textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(key);
        return textEncryptor;
    }
    private String deriveKey(String rawKey) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedKey = sha.digest(rawKey.getBytes());
            return bytesToHex(hashedKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash encryption key", e);
        }
    }
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String encrypt(String data, String rawKey) {
        if (data == null || rawKey == null) {
            throw new IllegalArgumentException("Data and key must not be null");
        }
        String derivedKey = deriveKey(rawKey);
        AES256TextEncryptor textEncryptor = getTextEncryptor(derivedKey);
        return textEncryptor.encrypt(data);
    }

    public String decrypt(String encryptedData, String rawKey) {
        if (encryptedData == null || rawKey == null) {
            throw new IllegalArgumentException("Encrypted data and key must not be null");
        }
        String derivedKey = deriveKey(rawKey);
        AES256TextEncryptor textEncryptor = getTextEncryptor(derivedKey);
        return textEncryptor.decrypt(encryptedData);
    }
}