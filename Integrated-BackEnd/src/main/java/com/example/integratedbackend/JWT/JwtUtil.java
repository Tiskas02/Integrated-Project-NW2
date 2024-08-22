package com.example.integratedbackend.JWT;

import com.example.integratedbackend.Users.UserRole;
import com.example.integratedbackend.Users.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Autowired
    private UserRepository userRepository;

    private String SECRET_KEY = "secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        String name = getNameByUsername(username);
        UUID oid = getOidByUsername(username);
        String email = getEmailByUsername(username);
        UserRole role = getRoleByUsername(username);

        claims.put("iss", "https://intproj23.sit.kmutt.ac.th/NW-2/");
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", new Date(System.currentTimeMillis() + 1000 * 60 * 30)); // 30 mins exp
        claims.put("name", name);
        claims.put("oid", oid);
        claims.put("email", email);
        claims.put("role", role);

        return createToken(claims, username);
    }

    private UserRole getRoleByUsername(String username) {
        return userRepository.findByUsername(username).getRole();
    }

    private String getEmailByUsername(String username) {
        return userRepository.findByUsername(username).getEmail();
    }

    private UUID getOidByUsername(String username) {
        return userRepository.findByUsername(username).getOid();
    }

    private String getNameByUsername(String username) {
        return userRepository.findByUsername(username).getName();
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours exp
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}

