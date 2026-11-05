package com.kelvin.spring_auth.configs;

import com.kelvin.spring_auth.services.JwtServices;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtServices jwtServices;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtServices jwtServices,
                                   HandlerExceptionResolver handlerExceptionResolver,
                                   UserDetailsService userDetailsService){
        this.handlerExceptionResolver=handlerExceptionResolver;
        this.jwtServices=jwtServices;
        this.userDetailsService=userDetailsService;

    }

    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
            ) throws ServletException, IOException {
        final String authHeader=request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        try{
            final String jwt=authHeader.substring(7);
            final String userEmail=jwtServices.extractUsername(jwt);

            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

            if(userEmail != null && authentication == null){
                UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);

                if(jwtServices.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);

        }
        catch (Exception exception){
            handlerExceptionResolver.resolveException(request,response,null,exception);
        }


    }
}
