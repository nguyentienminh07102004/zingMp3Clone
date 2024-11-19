package com.module5.zingMp3Clone.Service.User;

import com.module5.zingMp3Clone.Exception.DataInvalidException;
import com.module5.zingMp3Clone.Exception.ExceptionValue;
import com.module5.zingMp3Clone.Model.Entity.RoleEntity;
import com.module5.zingMp3Clone.Model.Entity.UserEntity;
import com.module5.zingMp3Clone.Model.Request.UserChangePassword;
import com.module5.zingMp3Clone.Model.Request.UserRequest;
import com.module5.zingMp3Clone.Model.Request.UserUpdateRequest;
import com.module5.zingMp3Clone.Model.Response.UserResponse;
import com.module5.zingMp3Clone.Repository.IRoleRepository;
import com.module5.zingMp3Clone.Repository.IUserRepository;
import com.module5.zingMp3Clone.Security.GeneratorToken;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;
    private final GeneratorToken generatorToken;

    @Override
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        if(!userRequest.getPassword().equals(userRequest.getRePassword())) {
            throw new DataInvalidException(ExceptionValue.PASSWORD_INVALID.getValue());
        }
        UserEntity user = modelMapper.map(userRequest, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        if(userRequest.getRoles() == null || userRequest.getRoles().isEmpty()) {
            user.setRoles(List.of(roleRepository.findById("USER")
                    .orElseGet(() -> roleRepository.save(new RoleEntity("USER", "Người dùng")))));
        }
        UserEntity userEntity = userRepository.save(user);
        return modelMapper.map(userEntity, UserResponse.class);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        if(userUpdateRequest.getId() == null || userUpdateRequest.getEmail() == null) {
            throw new DataInvalidException("Data invalid!");
        }
        UserEntity user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new DataInvalidException("User not found!"));
        if(!user.getEmail().equals(userUpdateRequest.getEmail())) {
            throw new DataInvalidException("Email invalid!");
        }
        UserEntity userEntity = modelMapper.map(userUpdateRequest, UserEntity.class);
        if(!userUpdateRequest.getRoles().isEmpty()) {
            List<RoleEntity> list = new ArrayList<>();
            userUpdateRequest.getRoles().forEach(role -> {
                RoleEntity roleEntity = roleRepository.findById(role)
                        .orElseThrow(() -> new DataInvalidException("Role not found!"));
                list.add(roleEntity);
            });
            userEntity.setRoles(list);
        }
        UserEntity userEntityUpdated = userRepository.save(userEntity);
        return modelMapper.map(userEntityUpdated, UserResponse.class);
    }

    @Override
    @Transactional
    public void deleteUser(List<String> ids) {
        for (String id : ids) {
            userRepository.findById(id)
                    .orElseThrow(() -> new DataInvalidException("User not found!"));
            userRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<UserResponse> getAllUsers(Integer page) {
        page = Math.max(page, 1);
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<UserEntity> pageEntity = userRepository.findAll(pageable);
        Page<UserResponse> pageResponse = pageEntity.map(entity -> modelMapper.map(entity, UserResponse.class));
        return new PagedModel<>(pageResponse);
    }


    @Override
    @Transactional
    public UserResponse changePassword(UserChangePassword userChangePassword) {
        UserEntity user = userRepository.findById(userChangePassword.getId())
                .orElseThrow(() -> new DataInvalidException("User not found!"));
        if(passwordEncoder.matches(userChangePassword.getOldPassword(), user.getPassword())) {
            throw new DataInvalidException("Old password is invalid!");
        }
        if(!userChangePassword.getNewPassword().equals(userChangePassword.getReNewPassword())) {
            throw new DataInvalidException("New password and repeat new password is invalid!");
        }
        user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        UserEntity userEntity = userRepository.save(user);
        return modelMapper.map(userEntity, UserResponse.class);
    }
}
