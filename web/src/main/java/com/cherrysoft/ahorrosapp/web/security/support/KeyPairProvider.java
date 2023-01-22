package com.cherrysoft.ahorrosapp.web.security.support;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface KeyPairProvider {

  RSAPublicKey getAccessTokenPublicKey();

  RSAPrivateKey getAccessTokenPrivateKey();

  RSAPublicKey getRefreshTokenPublicKey();

  RSAPrivateKey getRefreshTokenPrivateKey();

}
