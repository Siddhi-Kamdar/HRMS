package com.example.backend.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JWT Filter Executed");
        System.out.println("i am jwt i am the problem !!");


        String header = request.getHeader("Authorization");
        System.out.println("after authorization !!");

        if (header != null && header.startsWith("Bearer ")) {
            System.out.println("it goes in iff !!");

            String token = header.substring(7);

            try {
                System.out.println("its in try block!!");

                String email = jwtUtil.extractEmail(token);
                String role = jwtUtil.extractRole(token);

                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("Authenticated User: " + email);
                System.out.println("Authorities: " + authorities);

            } catch (Exception e) {
                System.out.println("and catch !!");

                System.out.println("JWT Validation Failed");
                e.printStackTrace();
            }
        }

        filterChain.doFilter(request, response);
    }
}