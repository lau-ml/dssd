package dssd.server.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final SecretKey secretKey;
    private final Cipher cipher;

    public EncryptionService(SecretKey secretKey, Cipher cipher) {
        this.secretKey = secretKey;
        this.cipher = cipher;
    }

    public String encrypt(String data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error during decryption", e);
        }
    }
}
