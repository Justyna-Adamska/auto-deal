package com.example.autodeal.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class Auth {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager get() {
        UserDetails user = User.withUsername("test")
                .password(passwordEncoder().encode("test"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(Arrays.asList(user, admin));
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/products").hasAnyRole("USER") //antMatchers: adresacja - zabezpieczeni adresów - kto gdzie ma dostęp
                .antMatchers("/orders").hasAnyRole("ADMIN")
                .antMatchers("/js/**", "css/**", "/vendor/**").permitAll()
                .antMatchers("/").permitAll()
                .and()
                .csrf().disable() //sprawia, że mozna spokojnie uderzać Postmanem i nie będzie pytał o logowanie
                .headers().frameOptions().disable()
                .and() //służy do łączenia bloków
                .formLogin() //informuje go, że teraz będę konfigurował formularz autoryzacji
                .loginPage("/login") //wskazuje endpoint w kßórym wyświetlam formularz logowania
                .usernameParameter("username") // nadaje nazwę jaka będzie jako name w inpucie loginu formularza
                .passwordParameter("password") //nadaje nazwę jaka będzie jako name w inpucie hasła formularza
                .loginProcessingUrl("/login")
                .failureForwardUrl("/login?error") //co się stanie w momencie wpisania błędnych danych
                .defaultSuccessUrl("/") //co się stanie w wypadku prawidłowego wpisania hasła
                .and()
                .logout()//mówimy springowi, że przechodzimy do obsługi wylogowania
                .logoutSuccessUrl("/"); //po wylogownaiu gdzie ma nas przekieorwać


        return http.build();



    }




}

