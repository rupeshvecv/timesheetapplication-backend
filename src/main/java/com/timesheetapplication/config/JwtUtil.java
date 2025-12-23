package com.timesheetapplication.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // ✅ SAME secret key as EmpowerEdge
    private static final String SECRET_KEY = "ThisIsAVeryStrongSecretKeyOfAtLeast32Chars";
    //private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours
    private static final long EXPIRATION = 15 * 60 * 1000; // 15 minutes

    private Key getSigningKey() {
    	return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
   	 //return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

	/*
	 * // ✅ Extract username (subject) public String extractUsername(String token) {
	 * return extractClaim(token, Claims::getSubject); }
	 */

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("JWT validation failed: " + e.getMessage());
            return false;
        }
    }
    
    // ✅ Extract expiration
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ✅ Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired", e);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT: " + e.getMessage(), e);
        }
    }

    // ✅ Validate token
	/*
	 * public boolean validateToken(String token, String username) { try { final
	 * String tokenUsername = extractUsername(token); return
	 * tokenUsername.equals(username) && !isTokenExpired(token); } catch
	 * (JwtException e) { System.out.println("JWT validation failed: " +
	 * e.getMessage()); return false; } }
	 */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
