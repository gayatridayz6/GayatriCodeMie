package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetail.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provider.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InMemoryUsersConfig {

    /**
     * MVP-style user store: no DB integration (explicitly skipped in story).
     * Username/passwords are encoded using the configured PasswordEncoder.
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails librarian = User.withUsername("librarian")
              .password(passwordEncoder.encode("libc123"))
              .roles(Role.LIBRARIAN.name())
            .build();

        UserDetails viewer = User.withUsername("viewer")
              .password(passwordEncoder.encode("view123"))
              .roles(Role.VIEWER.name())
            .build();

        return new InMemoryUserDetailsManager(librarian, viewer);
    }
}
