package com.kelvin.spring_auth.configs;

import com.kelvin.spring_auth.TokenAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, TokenAuthenticationEntryPoint entryPoint,
                                 JwtAuthenticationFilter jwtAuthenticationFilter){
        this.authenticationProvider=authenticationProvider;
        this.tokenAuthenticationEntryPoint=entryPoint;
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(c->c.disable())
                .authorizeHttpRequests(
                        auth->auth.requestMatchers("/users/register","/users/login","/error")
                                .permitAll().anyRequest().authenticated())
                .httpBasic(b->b.authenticationEntryPoint(tokenAuthenticationEntryPoint))
//                .httpBasic(Customizer.withDefaults())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(e->e.authenticationEntryPoint(
//                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
//                ))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e->e.authenticationEntryPoint(tokenAuthenticationEntryPoint))
                .build();
    }
}
