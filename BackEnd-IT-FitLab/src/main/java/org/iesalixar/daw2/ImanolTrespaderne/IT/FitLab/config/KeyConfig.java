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

    // Ruta al archivo del almacén de claves (keystore) definida en el archivo de configuración (application.properties o .yml)
    @Value("${jwt.keystore.path}")
    private String keystorePath;

    // Contraseña del almacén de claves
    @Value("${jwt.keystore.password}")
    private String keystorePassword;

    // Alias dentro del almacén de claves que identifica la clave deseada
    @Value("${jwt.keystore.alias}")
    private String keystoreAlias;

    /**
     * Bean que proporciona el par de claves (pública y privada) usado para firmar y verificar los tokens JWT.
     * Se carga desde un archivo de tipo JKS (Java KeyStore) configurado en las propiedades de la aplicación.
     *
     * @return KeyPair con la clave pública y la clave privada
     * @throws Exception en caso de fallo al leer el keystore
     */
    @Bean
    public KeyPair jwtKeyPair() throws Exception {
        // Cargar el keystore de tipo JKS
        KeyStore keyStore = KeyStore.getInstance("JKS");

        // Leer el archivo del keystore usando FileInputStream
        try (FileInputStream fis = new FileInputStream(keystorePath)) {
            keyStore.load(fis, keystorePassword.toCharArray()); // Cargar el contenido con la contraseña
        }

        // Obtener la clave privada asociada al alias
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keystoreAlias, keystorePassword.toCharArray());

        // Obtener la clave pública desde el certificado asociado al alias
        PublicKey publicKey = keyStore.getCertificate(keystoreAlias).getPublicKey();

        // Devolver el par de claves como un objeto KeyPair (clave pública y privada)
        return new KeyPair(publicKey, privateKey);
    }
}
