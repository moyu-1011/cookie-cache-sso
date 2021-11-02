package com.moyu.sc.utils;

import com.moyu.sc.config.AppProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final Key accessKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final Key refreshKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final AppProperties appProperties;

    public String createAccessToken(UserDetails userDetails) {
        return createJwtToken(userDetails, accessKey, appProperties.getJwt().getAccessTokenExpireTime());
    }

    public String createRefreshToken(UserDetails userDetails) {
        return createJwtToken(userDetails, refreshKey, appProperties.getJwt().getAccessRefreshTokenExpireTime());
    }

    public Optional<Jws<Claims>> parseClaims(String token) {
        return parseClaims(token, accessKey);
    }

    private Optional<Jws<Claims>> parseClaims(String token, Key key) {
        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token));
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private String createJwtToken(UserDetails userDetails, Key key, Long tokenExpireTime) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("moyu")
                .setAudience("app")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + tokenExpireTime))
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()))
                .signWith(key)
                .compact();
    }

    public Key getAccessKey() {
        return accessKey;
    }

    public Key getRefreshKey() {
        return refreshKey;
    }
}
