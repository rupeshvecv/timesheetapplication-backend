package com.edcapplication.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignAuthInterceptor implements RequestInterceptor {

    @Value("${internal.api.secret}")
    private String internalSecret;

    @Override
    public void apply(RequestTemplate template) {
        String token = null;

        //Try to get JWT token from current HTTP request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            token = request.getHeader("Authorization");
        }
        System.out.println("FeignAuthInterceptor.apply( JWT token from current HTTP request token ) "+token);

        //If no JWT (background thread), add internal service token
        if (token == null) {
            token = "Internal " + internalSecret;
        }
        System.out.println("FeignAuthInterceptor.apply(If no JWT (background thread), add internal service token) "+token);
        template.header("Authorization", token);
    }
}
