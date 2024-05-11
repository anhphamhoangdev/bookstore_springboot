package com.example.bookstore.security;

import com.example.bookstore.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {


    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService)
    {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configure ->
            configure // permit for all static file
                    .requestMatchers("/assets/**", "/.sass-cache/**","/fonts/**", "/img/**","/js/**", "/scss/**", "/style.css").permitAll()
                    .anyRequest().permitAll()
        ).formLogin(formLogin ->
            formLogin.loginPage("/login")
                    .loginProcessingUrl("/authenticateTheUser")
                    .successForwardUrl("/")
                    .permitAll()
        ).logout(
                logout-> logout.permitAll().logoutSuccessUrl("/")
        ).exceptionHandling(
                exception -> exception.accessDeniedPage("/show403page")
        ).rememberMe(rememberMe ->
                rememberMe
                        .key("remember-me")
                        .tokenValiditySeconds(3600)
        );

        return http.build();
    }


}
