package com.hedza06.saasscheduler.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedza06.saasscheduler.security.SecurityConfig.AccessViolationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UnauthorizedAccessHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException
  ) throws IOException, ServletException {
    var objectMapper = new ObjectMapper();

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(
        objectMapper.writeValueAsString(new AccessViolationResponse(
            accessDeniedException.getMessage(),
            "Unauthorized access detected. Access can not be granted!"
        ))
    );
  }
}
