package dssd.server.config;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptionConfig {

    private static final String ALGORITHM = "AES";

    @Value("${ENCRYPTION_KEY}") // Cargamos la clave desde las propiedades o variables de entorno
    private String encryptionKey;

    @Bean
    public SecretKey secretKey() {
        // Validar longitud de la clave (16 bytes para AES-128 o 32 bytes para AES-256)
        if (encryptionKey.length() != 16 && encryptionKey.length() != 32) {
            throw new IllegalArgumentException("La clave de encriptación debe ser de 16 o 32 caracteres.");
        }
        return new SecretKeySpec(encryptionKey.getBytes(), ALGORITHM);
    }

    @Bean
    public Cipher cipher() throws Exception {
        return Cipher.getInstance(ALGORITHM);
    }
}
