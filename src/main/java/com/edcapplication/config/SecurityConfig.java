/*
 * package com.edcapplication.config;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.annotation.web.configurers.
 * AbstractHttpConfigurer; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig {
 * 
 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception { http.csrf(AbstractHttpConfigurer::disable)
 * .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/public/**").permitAll() .anyRequest().authenticated() )
 * .addFilterBefore(new JwtAuthFilter(),
 * UsernamePasswordAuthenticationFilter.class);
 * 
 * return http.build(); } }
 */