/*
 * package com.edcapplication.config; import jakarta.servlet.*; import
 * jakarta.servlet.http.*; import org.springframework.security.authentication.
 * UsernamePasswordAuthenticationToken; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.core.authority.SimpleGrantedAuthority; import
 * org.springframework.stereotype.Component; import
 * org.springframework.web.filter.OncePerRequestFilter;
 * 
 * import java.io.IOException; import java.util.List;
 * 
 * @Component public class JwtAuthFilter extends OncePerRequestFilter {
 * 
 * @Override protected void doFilterInternal(HttpServletRequest request,
 * HttpServletResponse response, FilterChain filterChain) throws
 * ServletException, IOException { String header =
 * request.getHeader("Authorization"); if (header != null &&
 * header.startsWith("Bearer ")) { String token = header.substring(7); try { var
 * claims = JwtUtil.validateToken(token).getBody(); String username =
 * claims.getSubject(); String role = (String) claims.get("role");
 * 
 * var auth = new UsernamePasswordAuthenticationToken( username, null,
 * List.of(new SimpleGrantedAuthority("ROLE_" + role)) );
 * SecurityContextHolder.getContext().setAuthentication(auth);
 * 
 * } catch (Exception e) {
 * response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
 * return; } } filterChain.doFilter(request, response); } }
 */