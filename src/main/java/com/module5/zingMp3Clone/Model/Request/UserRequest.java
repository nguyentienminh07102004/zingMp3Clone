package com.module5.zingMp3Clone.Model.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank
    private String username;
    @Size(min = 6, max = 32)
    @NotBlank
    private String password;
    @Email
    private String email;
    private String phone;
    @NotBlank
    private String rePassword;
    private List<String> roles;
}
