package com.module5.zingMp3Clone.Service.User;

import com.module5.zingMp3Clone.Model.Request.UserChangePassword;
import com.module5.zingMp3Clone.Model.Request.UserRequest;
import com.module5.zingMp3Clone.Model.Request.UserUpdateRequest;
import com.module5.zingMp3Clone.Model.Response.UserResponse;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface IUserService {
    UserResponse save(UserRequest userRequest);
    UserResponse updateUser(UserUpdateRequest userUpdateRequest);
    void deleteUser(List<String> ids);
    PagedModel<UserResponse> getAllUsers(Integer page);
    UserResponse changePassword(UserChangePassword userChangePassword);
}
