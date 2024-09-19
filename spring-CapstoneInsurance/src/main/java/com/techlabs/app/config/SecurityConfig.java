package com.techlabs.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.techlabs.app.security.JwtAuthenticationEntryPoint;
import com.techlabs.app.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors()
            .and()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/E-Insurance/auth/**").permitAll() // Authentication endpoints (e.g., login, registration)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger access
                .requestMatchers("/E-Insurance/admin/**").hasRole("ADMIN") // Only Admins can access admin endpoints
                .requestMatchers("/E-Insurance/agent/**").hasRole("AGENT") // Only Agents can access agent endpoints
                .requestMatchers("/E-Insurance/employee/**").hasRole("EMPLOYEE") // Only Employees can access these endpoints
                .requestMatchers("/E-Insurance/customer/**").hasRole("CUSTOMER") // Only Customers can access these endpoints
                .requestMatchers("/E-Insurance/admin/reports/**").permitAll()  // Public admin reports API
                .requestMatchers("/E-Insurance/toall/**").permitAll()  // Public endpoint
                .requestMatchers(HttpMethod.GET, "/E-Insurance/file/").permitAll() 
                .requestMatchers(HttpMethod.POST, "/E-Insurance/file/").permitAll() 
                .requestMatchers(HttpMethod.GET, "/E-Insurance/file/view/{name}").permitAll()
                .anyRequest().authenticated() // All other requests require authentication
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions for JWT
            )
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before the default auth filter

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }
}
