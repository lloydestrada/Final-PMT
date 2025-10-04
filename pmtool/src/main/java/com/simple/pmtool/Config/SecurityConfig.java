package com.simple.pmtool.Config;

import com.simple.pmtool.Security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF for Ngrok/testing
                .csrf(csrf -> csrf.disable())
                // Apply CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Configure endpoint security
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/static/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/ngrok-test/**").permitAll()

                        // Role-based endpoints
                        .requestMatchers("/super/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")

                        // TEMP: Permit all requests coming from Ngrok for demo purposes
                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable());

        // Add JWT filter before Spring Security's authentication filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allow localhost (dev) + all Ngrok URLs for testing
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://steamiest-ellamae-lustrelessly.ngrok-free.dev",
                "https://steamiest-ellamae-lustrelessly.ngrok-free.dev/"

        ));

        // Allow common HTTP methods
        configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Apply this CORS config to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
