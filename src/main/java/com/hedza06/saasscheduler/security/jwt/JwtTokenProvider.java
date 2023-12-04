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

    var encryptKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    return Jwts.builder()
        .subject(subject)
        .claim(AUTH_KEY, "app-manager")
        .signWith(encryptKey)
        .expiration(accessTokenValidity)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    var decryptKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    Claims claims = Jwts.parser()
        .decryptWith(decryptKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();

    String username = claims.getSubject();
    Collection<? extends GrantedAuthority> authorities = Arrays
        .stream(claims.get(AUTH_KEY).toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .toList();

    return new UsernamePasswordAuthenticationToken(username, "", authorities);
  }

  public boolean isValid(String authToken) {
    var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    Jwts.parser().decryptWith(key).build().parse(authToken);
    return true;
  }
}
