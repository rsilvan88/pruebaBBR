package com.evertec.springboot2.crud.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    public Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @SuppressWarnings("deprecation")
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        List<String> roleNames = roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roleNames);

        return createToken(claims, userDetails.getUsername());
    }

    @SuppressWarnings("deprecation")
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public boolean validateToken(String token) {
        final String username = extractUsername(token);
        return (username != null && !isTokenExpired(token));
    }
}
