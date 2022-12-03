package com.usermanagerproj.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.usermanagerproj.config.ConfigProperties;
import com.usermanagerproj.config.security.SecurityConstants;
import com.usermanagerproj.service.user.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final ConfigProperties configProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        if(requestHeader == null || !requestHeader.startsWith(SecurityConstants.BEARER)){
            filterChain.doFilter(request, response);
            return;
        }
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.replace(SecurityConstants.BEARER, "");
        UUID user = UUID.fromString(JWT.require(Algorithm.HMAC512(configProperties.getJwtSecret()))
                .withIssuer(configProperties.getIssuer())
                .build()
                .verify(token)
                .getSubject());
        UserDetails userDetails = userDetailsService.loadUserById(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
