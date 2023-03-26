package com.stc.assessment.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
            auth -> auth.requestMatchers("/updateUserInfo")
                .permitAll().anyRequest().authenticated());

        http.httpBasic(withDefaults());

        http.csrf().disable();

        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user1")
            .password("{noop}secret1")
            .authorities("read")
            .roles("USER")
            .build();
        UserDetails userOne = User.withUsername("admin1")
            .password("{noop}secret1")
            .authorities("read")
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, userOne);
    }


}
