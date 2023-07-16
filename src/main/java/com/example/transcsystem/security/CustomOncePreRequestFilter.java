package com.example.transcsystem.security;

import com.example.transcsystem.utility.AppLog;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * @author
 * Created on July 08, 2023.
 * Time 11:29 PM
 */
@Component
@Slf4j
public class CustomOncePreRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if (!(request.getRequestURI().contains("h2") || request.getRequestURI().contains("swagger") || request.getRequestURI().contains("api-docs"))) {
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            CachedHttpRequestWrapper cachedHttpRequestWrapper = new CachedHttpRequestWrapper(request);

            //Capture token
            final String authHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION)).orElse("NONE");
            String requestUrl = request.getRequestURI();

            try {
                String body = StreamUtils.copyToString(cachedHttpRequestWrapper.getInputStream(), StandardCharsets.UTF_8);
                AppLog.setComment(this.getClass().getName(), body, "REQ");

            } catch (Exception e) {
                e.printStackTrace();
            }

            if(requestUrl.contains("registration") || requestUrl.contains("get-token") ) {
                //Since its get token it will basic auth which is the default auth
                filterChain.doFilter(cachedHttpRequestWrapper, responseWrapper);
            }else if (authHeader.contains("Bearer") && requestUrl.contains("/api/v1")) {
                // Any other request use Bearer token
                String token = authHeader.split(" ")[1];
                Map<String, String> resp = tokenService.verifyToken(token, request);
                if (resp.get("status").contains("VALID")) {

                    filterChain.doFilter(cachedHttpRequestWrapper, responseWrapper);
                    try {
                        String resBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
                        AppLog.setComment(this.getClass().getName(), resBody, "RES");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else  {
                    markResponseUnauthorised(response);
                }
            } else {
                 markResponseUnauthorised(response);
             }

            responseWrapper.copyBodyToResponse();

        }else {
            filterChain.doFilter(request, response);
        }
    }

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, 0, contentAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }



    public void markResponseUnauthorised(HttpServletResponse response) throws IOException {
        String Unauthorized = "Unauthorized";

        response.reset();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(Unauthorized.length());
        response.getWriter().write(Unauthorized);
        SecurityContextHolder.clearContext();
    }
}
