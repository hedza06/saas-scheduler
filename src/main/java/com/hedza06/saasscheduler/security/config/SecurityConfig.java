package com.hedza06.saasscheduler.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  @Order(1)
  public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
    return http
        .securityMatcher(AntPathRequestMatcher.antMatcher("/api/app/**"))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(withDefaults()) // Do not use in production environment!
        .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(
                    "/api-docs/**", "/scheduler-saas.html", "/swagger-ui/**"
                ).permitAll()

                // will be handled on controller level (pre-authorized)
                .requestMatchers("/api/auth/**").permitAll()

                // "Open" API (requires correct credentials)
                .requestMatchers("/api/admin/**").permitAll()

                .anyRequest().authenticated()
        )
        .build();
  }

  // TODO: define new security filter chain bean that will
  //  verify JWT token from Security Context Holder

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    var userDetails = User.builder()
        .username("hedza06")
        .password(passwordEncoder.encode("hedza123"))
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(userDetails);
  }

  public record AccessViolationResponse(String message, String localizedMessage) {
  }
}
