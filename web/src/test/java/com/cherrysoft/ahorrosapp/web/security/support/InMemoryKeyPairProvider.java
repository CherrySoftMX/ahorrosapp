package com.cherrysoft.ahorrosapp.web.security.support;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@ConditionalOnProperty(name = "in-memory.keypair", havingValue = "true")
public class InMemoryKeyPairProvider implements KeyPairProvider {
  private RSAKey accessTokenKeypair;
  private RSAKey refreshTokenKeypair;

  @Override
  public RSAPublicKey getAccessTokenPublicKey() {
    if (isNull(accessTokenKeypair)) {
      accessTokenKeypair = generateRsa();
    }
    try {
      return accessTokenKeypair.toRSAPublicKey();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public RSAPrivateKey getAccessTokenPrivateKey() {
    if (isNull(accessTokenKeypair)) {
      accessTokenKeypair = generateRsa();
    }
    try {
      return accessTokenKeypair.toRSAPrivateKey();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public RSAPublicKey getRefreshTokenPublicKey() {
    if (isNull(refreshTokenKeypair)) {
      refreshTokenKeypair = generateRsa();
    }
    try {
      return refreshTokenKeypair.toRSAPublicKey();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public RSAPrivateKey getRefreshTokenPrivateKey() {
    if (isNull(refreshTokenKeypair)) {
      refreshTokenKeypair = generateRsa();
    }
    try {
      return refreshTokenKeypair.toRSAPrivateKey();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  private RSAKey generateRsa() {
    KeyPair keyPair = generateRsaKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return new RSAKey.Builder(publicKey)
        .privateKey(privateKey)
        .keyID(UUID.randomUUID().toString())
        .build();
  }

  private KeyPair generateRsaKeyPair() {
    KeyPair keyPair;
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      keyPair = keyPairGenerator.generateKeyPair();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
    return keyPair;
  }

}
