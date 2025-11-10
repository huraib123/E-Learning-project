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
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    @Lazy
    private CustomUserDetailService customUserDetailService;

    // ✅ Define public paths that don’t need JWT authentication
    private static final String[] PUBLIC_PATHS = {
        "/api/auth/signup",
        "/api/auth/login",
        "/api/auth/verify",
        "/api/auth/resend-otp",
        "/uploads/",
        "/api/classes/uploads/"
    };
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth")
            || path.startsWith("/uploads")
            || path.startsWith("/api/courses/mycourses")
            
            || path.startsWith("/api/users");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        String path1 = request.getRequestURI();
//        if (path1.startsWith("/videos/") || path1.startsWith("/uploads/")|| path.startsWith("/api/courses/allcourses/**")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
        System.out.println("Authorization header: " + request.getHeader("Authorization"));


        // ✅ Skip JWT validation for public paths
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // ✅ Proceed with JWT validation
        String token = jwtTokenProvider.getTokenFromRequest(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromToken(token);

            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

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
