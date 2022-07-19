package com.example.jbcn_as.config.keys;

import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.stereotype.Component;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Component
public class KeyManager {

  public RSAKey getKey() {
    try {
      KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
      g.initialize(2048);
      var kp = g.generateKeyPair();

      RSAPublicKey rsaPublicKey = (RSAPublicKey) kp.getPublic();
      RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) kp.getPrivate();

      return new RSAKey.Builder(rsaPublicKey).privateKey(rsaPrivateKey)
          .keyID(UUID.randomUUID().toString())
          .build();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(":(");
    }
  }
}
