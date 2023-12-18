package com.hedza06.saasscheduler.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtTokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    try {
      var tokenOptional = resolveTokenFromRequest(request);
      if (tokenOptional.isPresent() && tokenProvider.isValid(tokenOptional.get())) {
        Authentication authentication = tokenProvider.getAuthentication(tokenOptional.get());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException e) {
      prepareExpiredTokenResponse(response, e);
    }
  }

  private Optional<String> resolveTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return Optional.of(bearerToken.substring(7));
    }
    return Optional.empty();
  }

  private void prepareExpiredTokenResponse(HttpServletResponse response, ExpiredJwtException e)
      throws IOException {
    var expiredJwtResponse = new ExpiredJwtResponse(e.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(expiredJwtResponse)
    );
  }

  private record ExpiredJwtResponse(String message) {
  }
}
