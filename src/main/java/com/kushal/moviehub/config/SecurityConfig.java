package com.kushal.moviehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kushal.moviehub.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        // Authentication
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // User registration
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        )
                        .permitAll()

                        // Public movie GET APIs
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/movies/**"
                        )
                        .permitAll()

                        // Movie management - ADMIN only
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/movies/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/movies/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/movies/**"
                        )
                        .hasRole("ADMIN")

                        // Reviews
                        .requestMatchers(
                                "/api/reviews/**",
                                "/api/movies/*/reviews"
                        )
                        .hasAnyRole("USER", "ADMIN")

                        // Watchlist
                        .requestMatchers(
                                "/api/users/*/watchlist/**"
                        )
                        .hasAnyRole("USER", "ADMIN")

                        // User management - ADMIN only
                        .requestMatchers(
                                "/api/users/**"
                        )
                        .hasRole("ADMIN")

                        // Everything else requires authentication
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form.disable())

                .httpBasic(basic -> basic.disable());

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}