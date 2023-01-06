package com.cherrysoft.ahorrosapp.security.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import static java.util.Objects.isNull;

@Component
public class KeyPairProvider {
  @Value("${access-token.private}")
  private String accessTokenPrivateKeyPath;

  @Value("${access-token.public}")
  private String accessTokenPublicKeyPath;

  @Value("${refresh-token.private}")
  private String refreshTokenPrivateKeyPath;

  @Value("${refresh-token.public}")
  private String refreshTokenPublicKeyPath;

  private KeyPair accessTokenKeyPair;
  private KeyPair refreshTokenKeyPair;

  private KeyPair getAccessTokenKeyPair() {
    if (isNull(accessTokenKeyPair)) {
      var keyPairRepository = new KeyPairRepository(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
      accessTokenKeyPair = keyPairRepository.getKeyPair();
    }
    return accessTokenKeyPair;
  }

  private KeyPair getRefreshTokenKeyPair() {
    if (isNull(refreshTokenKeyPair)) {
      var keyPairRepository = new KeyPairRepository(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
      refreshTokenKeyPair = keyPairRepository.getKeyPair();
    }
    return refreshTokenKeyPair;
  }

  public RSAPublicKey getAccessTokenPublicKey() {
    return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
  }

  public RSAPrivateKey getAccessTokenPrivateKey() {
    return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
  }

  public RSAPublicKey getRefreshTokenPublicKey() {
    return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
  }

  public RSAPrivateKey getRefreshTokenPrivateKey() {
    return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
  }

}
