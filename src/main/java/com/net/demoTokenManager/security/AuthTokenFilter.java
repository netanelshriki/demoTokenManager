package com.net.demoTokenManager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseCookie(request);
        System.out.println("token: "+token);

        if (token != null && jwtUtils.validateJwtToken(token)){

            String email = jwtUtils.getEmailFromToken(token);


        }


    }



    private String parseCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cook : cookies) {
            if (cook.getName().equals("jwt")) {
                return cook.getValue();
            }
        }
        return null;
    }

}
