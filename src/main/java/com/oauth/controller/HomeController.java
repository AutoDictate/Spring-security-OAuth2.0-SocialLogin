package com.oauth.controller;

import com.oauth.UserDetails;
import com.oauth.security.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String hello() {
        return "Hello, from Backend OAuth 2.0 login";
    }

    @GetMapping("get")
    public Principal user(Principal principal) {
        System.out.println(principal);
        return principal;
    }

    @GetMapping("/user")
    public UserDetails userDetails(Principal principal) {
        return userService.getUserDetails(principal);
    }

    @GetMapping("/token")
    public String getAccessToken(Authentication authentication) {
        return userService.getAccessToken(authentication);
    }
}
