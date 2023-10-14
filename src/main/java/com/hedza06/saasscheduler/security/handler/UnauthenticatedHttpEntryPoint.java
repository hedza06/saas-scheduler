package com.hedza06.saasscheduler.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedza06.saasscheduler.security.SecurityConfig.AccessViolationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthenticatedHttpEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException
  ) throws IOException, ServletException {
    var objectMapper = new ObjectMapper();

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(
        objectMapper.writeValueAsString(new AccessViolationResponse(
            "Unauthenticated access",
            authException.getLocalizedMessage()
        ))
    );
  }
}
