package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Request.AuthenticationRequest;
import com.module5.zingMp3Clone.Model.Request.UserRequest;
import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Model.Response.UserResponse;
import com.module5.zingMp3Clone.Service.Authentication.JwtAuthenticationService;
import com.module5.zingMp3Clone.Service.Playlist.PlaylistService;
import com.module5.zingMp3Clone.Service.User.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtAuthenticationService authenticationService;
    private final IUserService userService;
    private final PlaylistService playlistService;

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody AuthenticationRequest request, HttpServletResponse response) {
        String token = authenticationService.authenticate(request, response);
        var apiResponse = APIResponse.builder()
                .message("Đăng nhập thành công")
                .data(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping(value = "/register")
    private ResponseEntity<APIResponse> saveUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) {
        UserResponse userResponse = userService.save(userRequest);
        playlistService.createFavoritesPlaylist(userRequest.getUsername());
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
