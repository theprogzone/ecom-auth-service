package com.ecommerce.ecom_auth_service.util;

import com.ecommerce.ecom_auth_service.exception.ValidationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JWTUtil {

    private final String SECRET_KEY = "ABCDEFGHI1234567890JKLMNOPQRST1234567890";

    public String generateJwtToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims) // Setting some metadata. Ex: User email, user role
                .subject(subject) // Setting the token subject as email of the user
                .issuedAt(new Date(System.currentTimeMillis())) // Token creation time
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // Token expiration time is 30 minutes
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public void validateToken(String token) {
        // Check whether the token is expired or not. If expired then throw ValidationException
        if (isTokenExpired(token)) {
            log.error("Token expired");
            throw new ValidationException("Token expired");
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a single claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getKey())
                .build();
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
