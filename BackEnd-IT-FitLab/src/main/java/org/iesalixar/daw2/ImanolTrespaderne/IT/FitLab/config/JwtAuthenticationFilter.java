package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.CustomUserDetailService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
@Autowired
    private JwtUtil jwtutil;
@Autowired
    private CustomUserDetailService customUserDetailService;
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
    final String authHeader=request.getHeader("Authorization");
    final String jwt;
    final String username;

    if(authHeader ==null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request,response);
        return;
    }
    jwt=authHeader.substring(7);
    username=jwtutil.extractUsername(jwt);

    if(username != null && SecurityContextHolder.getContext().getAuthentication()==null){
        var userDetails= customUserDetailService.loadUserByUsername(username);

        if(jwtutil.validateToken(jwt, userDetails.getUsername())){
            Claims claims= jwtutil.extractAllClaims(jwt);

            List<String> roles= claims.get("roles", List.class);
            List<SimpleGrantedAuthority> authorities =  roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
    filterChain.doFilter(request,response);
}



}
