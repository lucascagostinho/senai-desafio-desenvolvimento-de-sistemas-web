package com.travelagency.destinations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder é o padrão recomendado pelo Spring Security.
     * É intencionalmente lento para resistir a ataques de força bruta.
     * Declarado como @Bean para ser injetado no UserService via @Autowired.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define as regras de acesso por endpoint e ativa o HTTP Basic Authentication.
     *
     * Regras aplicadas:
     * - POST /users/register → público (qualquer um pode se cadastrar)
     * - GET  /destinations/** → público (leitura de destinos não exige login)
     * - POST/PUT/DELETE /destinations/** → apenas role ADMIN
     * - /reviews/** → qualquer usuário autenticado
     * - qualquer outra rota → autenticado
     *
     * CSRF desabilitado: proteção contra CSRF é para apps com sessão de browser.
     * APIs REST stateless com HTTP Basic não são vulneráveis a CSRF.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/destinations/**").permitAll()
                .requestMatchers("/destinations/**").hasRole("ADMIN")
                .requestMatchers("/reviews/**").authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
