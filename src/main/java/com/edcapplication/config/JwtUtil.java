/*
 * package com.edcapplication.config;
 * 
 * import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys;
 * 
 * import java.nio.charset.StandardCharsets; import java.security.Key; import
 * java.util.Date; import java.util.List;
 * 
 * public class JwtUtil {
 * 
 * // <--- Replace this with a property from application.properties in real apps
 * private static final String SECRET =
 * "change-this-to-a-very-long-secret-key-at-least-32-bytes!"; private static
 * final long EXP_MS = 1000L * 60 * 60; // 1 hour private static final Key KEY =
 * Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
 * 
 * public static String generateToken(String username, List<String> roles) {
 * return Jwts.builder() .setSubject(username) .claim("roles", roles)
 * .setIssuedAt(new Date()) .setExpiration(new Date(System.currentTimeMillis() +
 * EXP_MS)) .signWith(KEY, SignatureAlgorithm.HS256) .compact(); }
 * 
 * public static Jws<Claims> validateToken(String token) throws JwtException {
 * return Jwts.parserBuilder() .setSigningKey(KEY) .build()
 * .parseClaimsJws(token); } }
 */