package com.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;


public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String clientIP = req.getRemoteAddr();
        StringBuilder headers = new StringBuilder();

        req.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            headers.append(headerName)
                    .append(": ")
                    .append(req.getHeader(headerName))
                    .append("; ");
        });

        logger.info("Request - IP: {}, Method: {}, URI: {}, Headers: {}",
                clientIP, req.getMethod(), req.getRequestURI(), headers);

        chain.doFilter(request, response); // ادامه پردازش
    }
}
