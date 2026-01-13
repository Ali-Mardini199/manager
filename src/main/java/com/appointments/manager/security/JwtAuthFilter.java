package com.appointments.manager.security;

import com.appointments.manager.controller.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private CustomUserDetailsService userDetailsService;

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {
                String path = request.getServletPath();
                if (path.startsWith("/swagger-ui")
                                || path.startsWith("/v3/api-docs")
                                || path.startsWith("/h2-console")
                                || path.startsWith("/auth")
                                || path.startsWith("/ws")) {
                        filterChain.doFilter(request, response);
                        return;
                }
                String authHeader = request.getHeader("Authorization");

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                        filterChain.doFilter(request, response);
                        return;
                }

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        String username = jwtUtil.extractUsername(token);

                        if (username != null &&
                                        SecurityContextHolder.getContext().getAuthentication() == null) {

                                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                                if (jwtUtil.isTokenValid(token, userDetails)) {
                                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                        userDetails, null, userDetails.getAuthorities());

                                        authToken.setDetails(
                                                        new WebAuthenticationDetailsSource().buildDetails(request));

                                        SecurityContextHolder.getContext().setAuthentication(authToken);
                                }
                        }
                }

                filterChain.doFilter(request, response);
        }
}
