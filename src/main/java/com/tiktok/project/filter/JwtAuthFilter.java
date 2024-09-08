package com.tiktok.project.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiktok.project.service.ImpService.UserImpService;
import com.tiktok.project.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    @Lazy
    private UserImpService userImpService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        String username = null;

        // Kiểm tra xem header Authorization có hợp lệ không
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // "Bearer " dài 7 ký tự
            try {
                // Lấy username từ token
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                // Xử lý lỗi khi lấy username từ token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", "Invalid token");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
                return;
            }
        }

        // Nếu có username và không có Authentication trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userImpService.loadUserByUsername(username);

            // Kiểm tra tính hợp lệ của token
            if (jwtService.validateToken(token, userDetails)) {
                // Tạo AuthenticationToken và thiết lập vào SecurityContext
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                // Nếu token không hợp lệ
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                Map<String, String> error = new HashMap<>();
                error.put("error_message", "Invalid token");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
                return;
            }
        }

        // Tiếp tục với chuỗi filter
        filterChain.doFilter(request, response);
    }

}
