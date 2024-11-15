package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Request.AuthenticationRequest;
import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Service.Authentication.JwtAuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtAuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody AuthenticationRequest request, HttpServletResponse response) {
        String token = authenticationService.authenticate(request, response);
        var apiResponse = APIResponse.builder()
                .message("Đăng nhập thành công")
                .data(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
