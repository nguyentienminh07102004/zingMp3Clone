package com.module5.zingMp3Clone.API;

import com.module5.zingMp3Clone.Model.Request.UserChangePassword;
import com.module5.zingMp3Clone.Model.Request.UserLoginRequest;
import com.module5.zingMp3Clone.Model.Request.UserRequest;
import com.module5.zingMp3Clone.Model.Request.UserUpdateRequest;
import com.module5.zingMp3Clone.Model.Response.APIResponse;
import com.module5.zingMp3Clone.Model.Response.UserResponse;
import com.module5.zingMp3Clone.Service.User.IUserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/${api.prefix}/users")
@RequiredArgsConstructor
public class UserAPI {
    private final IUserService userService;

    @PostMapping(value = "/register")
    private ResponseEntity<APIResponse> saveUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) {
        UserResponse userResponse = userService.save(userRequest);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/update-profile")
    @PostAuthorize(value = "returnObject.body.data.email.equals(authentication.name) || hasRole('ADMIN')")
    public ResponseEntity<APIResponse> updateUser(@Valid @RequestBody UserUpdateRequest userRequest, BindingResult result) {
        UserResponse userResponse = userService.updateUser(userRequest);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{ids}")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<APIResponse> deleteUser(@PathVariable List<String> ids) {
        userService.deleteUser(ids);
        APIResponse response = APIResponse.builder()
                .message("DELETE SUCCESS")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/")
    @PermitAll
    public ResponseEntity<APIResponse> getAllUsers(@RequestParam(required = false) Integer page) {
        PagedModel<UserResponse> list = userService.getAllUsers(page);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(list)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<APIResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest, BindingResult result) {
        String token = userService.login(userLoginRequest);
        APIResponse response = APIResponse.builder()
                .message("LOGIN SUCCESS")
                .data(token)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/change-password")
    @PostAuthorize(value = "returnObject.body.data.id.equals(authentication.name) || hasRole('ADMIN')")
    public ResponseEntity<APIResponse> changePassword(@Valid @RequestBody UserChangePassword userChangePassword, BindingResult result) {
        UserResponse userResponse = userService.changePassword(userChangePassword);
        APIResponse response = APIResponse.builder()
                .message("CHANGE PASSWORD SUCCESS")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
