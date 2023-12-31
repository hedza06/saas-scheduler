package com.hedza06.saasscheduler.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  private static final String AUTH_KEY = "auth";

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.validityInMinutes}")
  private long tokenValidityInMinutes;


  public String generateToken(String subject) {
    long now = new Date().getTime();
    Date accessTokenValidity = new Date(now + tokenValidityInMinutes * 60_000);

    return Jwts.builder()
        .subject(subject)
        .claim(AUTH_KEY, "app-manager")
        .signWith(getSecretKey())
        .issuedAt(new Date())
        .expiration(accessTokenValidity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    String subject = claims.getSubject();
    Collection<? extends GrantedAuthority> authorities = Arrays
        .stream(claims.get(AUTH_KEY).toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .toList();

    return new UsernamePasswordAuthenticationToken(subject, "", authorities);
  }

  public boolean isValid(String authToken) {
    Jwts.parser().verifyWith(getSecretKey()).build().parse(authToken);
    return true;
  }

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
