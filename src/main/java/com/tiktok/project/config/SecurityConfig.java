package com.tiktok.project.config;

import com.tiktok.project.filter.JwtAuthFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    @Lazy
    private CustomAuthenticationProvider customAuthenticationProvider;

    private final String[] WHITE_LIST = {"/auth/**",
                                    "/comments/get/**",
                                    "/follow/checkFollowing",
                                    "/follow/get-list-following",
                                    "/follow/get-list-follower",
                                    "/users/get/**",
                                    "/users/search",
                                    "/video/feed",
                                    "/video/all/**",
                                    "/video/getByUser",
                                    "/video/getByUsername&VideoId/**",
                                    "/views/**"
                                    };
    private final String[] BLACK_LIST = {"/messages/**",
                                    "/comments/post",
                                    "/follow/toggleFollow",
                                    "/users/profile",
                                    "/users/check-username",
                                    "/users/edit-profile",
                                    "/users/get-list-friend",
                                    "/video/upload",
                                    "/video/like",
                                    "/video/collect"
                                    };

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
        JwtAuthFilter jwtAuthFilter = applicationContext.getBean(JwtAuthFilter.class);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ws/**").permitAll() // 🔥 Mở WebSocket
                        .requestMatchers(BLACK_LIST).authenticated()
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().denyAll()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("http://localhost:3000"); // Thay thế bằng domain cụ thể
        config.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP
        config.addAllowedHeader("*"); // Cho phép tất cả các header
        config.setAllowCredentials(true); // Cho phép credentials
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }



}