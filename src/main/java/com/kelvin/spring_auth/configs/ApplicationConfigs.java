package com.kelvin.spring_auth.configs;

import com.kelvin.spring_auth.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class ApplicationConfigs {

    private final UserRepository userRepository;

    public ApplicationConfigs(UserRepository userRepository){
        this.userRepository=userRepository;

    }
    @Bean
    UserDetailsService userDetailsService(){
        return email -> userRepository.findByEmail(email)
                .map(u-> User.withUsername(u.getEmail())
                        .password(u.getPassword())
                        .build())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configs) throws Exception{
        return configs.getAuthenticationManager();

    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authprovider= new DaoAuthenticationProvider(userDetailsService());
        authprovider.setPasswordEncoder(passwordEncoder());
        return authprovider;
    }


}
