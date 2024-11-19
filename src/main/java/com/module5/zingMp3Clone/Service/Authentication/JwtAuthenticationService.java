package com.module5.zingMp3Clone.Service.Authentication;

import com.module5.zingMp3Clone.Exception.ExceptionValue;
import com.module5.zingMp3Clone.Model.Entity.UserEntity;
import com.module5.zingMp3Clone.Model.Request.AuthenticationRequest;
import com.module5.zingMp3Clone.Repository.IUserRepository;
import com.module5.zingMp3Clone.Security.GeneratorToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GeneratorToken generatorToken;

    public String authenticate (AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        UserEntity user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow(() ->
                new RuntimeException(ExceptionValue.USER_NOT_FOUND.getValue()));
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new RuntimeException(ExceptionValue.PASSWORD_INVALID.getValue());
        }
        var token = generatorToken.generatorToken(user);
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);
        response.addCookie(cookie);
        return token;
    }
}
