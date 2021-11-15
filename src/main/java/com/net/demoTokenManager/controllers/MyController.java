package com.net.demoTokenManager.controllers;

import com.net.demoTokenManager.beans.Project;
import com.net.demoTokenManager.beans.User;
import com.net.demoTokenManager.repos.ProjectRepository;
import com.net.demoTokenManager.repos.UserRepository;
import com.net.demoTokenManager.securityold.Information;
import com.net.demoTokenManager.security.JWTUtils;
import com.net.demoTokenManager.securityold.TokenManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MyController {
    private final UserRepository userRepository;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private JWTUtils jwtUtils;

    private final ProjectRepository projectRepository;

    //    @PostMapping("register")
    public String register(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            User userToToken = userRepository.findByEmail(user.getEmail());
            return jwtUtils.generateToken(userToToken);
        }

        userRepository.save(user);
        User userToToken = userRepository.findByEmail(user.getEmail());
        return jwtUtils.generateToken(userToToken);
    }


    @GetMapping("info")
    @ResponseStatus(HttpStatus.OK)
    public Information getInfo(@RequestHeader(name = "Authorization") String token) {
        return tokenManager.getInfoFromToken(token);
    }

    @GetMapping("security/{projectId}")
    public Project isAllowed(@RequestHeader(name = "Authorization") String token, @PathVariable int projectId) throws Exception {
        if (!jwtUtils.isAllowed(token, projectId)) {
            throw new Exception("you are not allowed");
        }
        return projectRepository.getOne(projectId);
    }

    @GetMapping("parse")
    public Claims parseToken(@RequestHeader(name = "Authorization") String token) {
        return jwtUtils.getClaimFromToken(token);
    }

    @GetMapping("add/cookie")
    public ResponseEntity responseCookie(@RequestHeader(name = "Authorization") String token) {

        ResponseCookie springCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(36000)
                .domain("localhost")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .build();
    }

    @GetMapping("get/cookie")
    public String getFromCookies(@CookieValue(name = "jwt") String jwt) {
        return jwt;
    }


    @PostMapping("register")
    public ResponseEntity<?> fixedRegister(@RequestBody User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            User userToToken = userRepository.findByEmail(user.getEmail());
            String token = jwtUtils.generateToken(userToToken);

            ResponseCookie springCookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(1000 * 60 * 60 * 24 * 3)
                    .domain("localhost")
                    .build();

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).build();
        }

        userRepository.save(user);
        User userToToken = userRepository.findByEmail(user.getEmail());
        String token = jwtUtils.generateToken(userToToken);

        ResponseCookie springCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(1000 * 60 * 60 * 24 * 3)
                .domain("localhost")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, springCookie.toString()).build();
    }

    @GetMapping("projects/{projectId}")
    public Project getAllProjects(@CookieValue(name = "jwt") String jwt, @PathVariable int projectId) throws Exception {
        if (!jwtUtils.isAllowed(jwt, projectId)) {
            throw new Exception("you are not allowed");
        }
        return projectRepository.getOne(projectId);
    }


}
