package com.example.SkillCore.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwttokenp;

    @Autowired
    @Lazy
    private CustomUserDetailService customuserdetail;
    

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ Skip JWT check for public endpoints
        if (path.startsWith("/api/auth/signup") ||
            path.startsWith("/api/auth/login") ||  
            path.startsWith("/api/auth/verify")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Continue JWT validation for protected endpoints
        String token = jwttokenp.getTokenFromRequest(request);

        if (token != null && jwttokenp.validateToken(token)) {
            String username = jwttokenp.getUsernameFromToken(token);

            UserDetails userDetails = customuserdetail.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
