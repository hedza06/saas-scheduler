package com.hedza06.saasscheduler.security.config;

import com.hedza06.saasscheduler.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Value("${security.username}")
  private String username;

  @Value("${security.password}")
  private String password;


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
        .securityMatcher("/api/auth/**", "/api/admin/**")
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

  @Bean
  @Order(3)
  public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http, JwtFilter jwtFilter)
      throws Exception {
    return http
        .securityMatcher("/api/job-creator/**")
        .csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
    var userDetails = User.builder()
        .username(username)
        .password(passwordEncoder.encode(password))
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(userDetails);
  }

  public record AccessViolationResponse(String message, String localizedMessage) {
  }
}
