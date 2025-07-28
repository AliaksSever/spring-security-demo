package com.itechart.springsecuritydemo.service;

import com.itechart.springsecuritydemo.repository.JwtRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService implements JwtRepository {

    private final SecretKey secretKey;


    public JwtService(@Value("${jwt.token}")String secretKey) {
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("Secret must be at least 32 characters for HS256");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public String generateToken(Authentication authentication) {
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("scope", scope)
                .setIssuer("self")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000)) // 1 час
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

    }
    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        return jws.getBody();
    }
}