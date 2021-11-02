package com.moyu.sc.security.filter;

import com.moyu.sc.config.AppProperties;
import com.moyu.sc.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final AppProperties appProperties;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if (checkJwtToken(req)) {
            jwtUtil.parseClaims(obtainJwtToken(req))
                    .filter(claimsJws -> null != claimsJws.getBody().get("authorities"))
                    .ifPresent(this::setAuthenticated);
        }
        filterChain.doFilter(req, res);
    }

    private void setAuthenticated(Jws<Claims> claimsJws) {
        Object auth = claimsJws.getBody().get("authorities");
        Set<SimpleGrantedAuthority> authorities = Stream.of(auth)
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(claimsJws.getBody().getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private String obtainJwtToken(HttpServletRequest req) {
        if (!checkJwtToken(req)) {
            return "";
        }
        return req.getHeader(appProperties.getJwt().getHeader()).replace(appProperties.getJwt().getPrefix(), "");
    }

    // 检查header是否存在jwtToken
    private boolean checkJwtToken(HttpServletRequest req) {
        String authenticationHeader = req.getHeader(appProperties.getJwt().getHeader());
        return authenticationHeader != null && authenticationHeader.startsWith(appProperties.getJwt().getPrefix());
    }
}
