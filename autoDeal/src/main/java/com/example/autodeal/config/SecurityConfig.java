package com.example.autodeal.config;

import com.example.autodeal.cart.CartService;
import com.example.autodeal.user.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {

    private String secret;


    public SecurityConfig(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CartService cartService, UserService userService) throws Exception {
        http
//                .csrf().disable()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //
//
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/user/orders").authenticated()
                        .requestMatchers("/cart/**").authenticated() // Tylko uwierzytelnieni użytkownicy mają dostęp do ścieżek koszyka
                        .anyRequest().permitAll())
//                .addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService, secret))
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessHandler(logoutSuccessHandler(cartService, userService))
                        .permitAll());
        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(CartService cartService, UserService userService) {
        return new CustomLogoutSuccessHandler(cartService, userService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));

    }
}