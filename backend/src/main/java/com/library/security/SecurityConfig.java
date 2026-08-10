package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.BeCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests (auth -> auth
                    .requestMatchers("/api/health", "/h2-console/**").PermitAll()

                    // All api endpoints require authentication
                    .requestMatchers("/api/**").authenticated()

                    // MODIFY operations need LIBRARIAN
                    .requestMatchers(HttpMethod.POST, "/api/books").hasRole(Role.LIBRARIAN.name())
                    .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole(Role.LIBRARIAN.name())
                    .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole(Role.LIBRARIAN.name())

                    .anyRequest().denyAll()
            )
            .httpBasic(Customizer.withDefaults());

        // Here for h2-console frames support
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCnyptPasswordEncoder();
    }
}
