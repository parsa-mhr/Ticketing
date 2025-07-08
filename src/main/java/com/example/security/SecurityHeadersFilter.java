package com.example.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // تنظیم هدر XSS Protection
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block");

        // حذف هدرهای حساس
        httpResponse.setHeader("X-Powered-By", "");
        httpResponse.setHeader("Server", "");

        chain.doFilter(request, response);
    }
}