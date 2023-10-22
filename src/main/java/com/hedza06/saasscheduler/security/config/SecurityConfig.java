package com.hedza06.saasscheduler.security.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement
            -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            authorize -> authorize
                // TODO: must be admin...
                .requestMatchers("/api/app/**").hasRole(AuthRole.ADMIN.getSuffix())

                // will be handled on controller level
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").permitAll()

                .anyRequest().authenticated()
        ).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public record AccessViolationResponse(String message, String localizedMessage) {
  }

  @Getter
  @RequiredArgsConstructor
  public enum AuthRole {
    ADMIN("ROLE_ADMIN", "ADMIN"),
    USER("ROLE_USER", "USER");

    private final String role;
    private final String suffix;
  }
}
