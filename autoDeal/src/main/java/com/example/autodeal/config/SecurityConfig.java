package com.example.autodeal.config;

import com.example.autodeal.model.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {

    private String secret;

    public SecurityConfig(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) throws Exception {
        http
                .csrf().disable()  // Wyłączenie ochrony CSRF
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // Ustawienie polityki sesji na bezstanową
                .and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole(UserRole.ROLE_ADMIN.getRole())
                        .requestMatchers(HttpMethod.GET, "/orders").authenticated()
                        .anyRequest().permitAll())
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService, secret))  // Dodanie niestandardowego filtra JWT
                .formLogin(form -> form  // Konfiguracja formularza logowania
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(logout -> logout  // Konfiguracja wylogowania
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll());

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}