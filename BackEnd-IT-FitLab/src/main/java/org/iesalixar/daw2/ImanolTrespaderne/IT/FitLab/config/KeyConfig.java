package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;


@Configuration
public class KeyConfig {
@Value("${jwt.keystore.path}")
    private String keystorePath;
    @Value("${jwt.keystore.password}")
    private String keystorePassword;
    @Value("${jwt.keystore.alias}")
    private String keystoreAlias;

    @Bean
    public KeyPair jwtKeyPair() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keystoreAlias, keystorePassword.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(keystoreAlias).getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }
}
