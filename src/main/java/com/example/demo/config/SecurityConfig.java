package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/uploads/**", "/css/**", "/js/**", "/images/**", "/403").permitAll()
                .requestMatchers(HttpMethod.POST, "/products", "/product/add").hasRole("ADMIN")
                .requestMatchers(
                    "/product/add", "/products/new", "/products/edit/**", "/products/delete/**",
                    "/categories/new", "/categories/edit/**", "/categories/delete/**",
                    "/admin/orders/**"
                ).hasRole("ADMIN")
                .requestMatchers("/home", "/products/**", "/categories/**", "/cart/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception.accessDeniedPage("/403"))
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .httpBasic(Customizer.withDefaults())
            .logout(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
