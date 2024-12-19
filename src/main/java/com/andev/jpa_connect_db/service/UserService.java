package com.andev.jpa_connect_db.service;

import com.andev.jpa_connect_db.dto.request.ApiResponse;
import com.andev.jpa_connect_db.dto.request.UserCreationRequest;
import com.andev.jpa_connect_db.dto.response.UserResponse;
import com.andev.jpa_connect_db.entity.User;
import com.andev.jpa_connect_db.enums.Role;
import com.andev.jpa_connect_db.exception.AppException;
import com.andev.jpa_connect_db.exception.ErrorCode;
import com.andev.jpa_connect_db.exception.UserNotFoundException;
import com.andev.jpa_connect_db.mapper.UserMapper;
import com.andev.jpa_connect_db.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse creatUser(UserCreationRequest reObject){

        if(userRepository.existsByUsername(reObject.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(reObject);
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(reObject.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ApiResponse<List<User>> getListUser(){
        log.info("In method get user ");
        return ApiResponse.<List<User>>builder().result(userRepository.findAll()).build();
    }


    // chỉ trả về dữ liệu khi người đăng nhập có role là "ROLE_ADMIN" hoặc dữ liệu trả về trùng tên với người đăng nhập
    @PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.username==authentication.name")
    public UserResponse getById(String Id){
        log.info("In method get user by ID ");
        return userMapper.toUserResponse(userRepository.findById(Id).orElseThrow(() -> new UserNotFoundException("User with id " + Id + " not found"))) ;
    }

    public UserResponse updateUser(UserCreationRequest newUser, String Id){
        User user =userRepository.findById(Id).orElseThrow(() -> new UserNotFoundException("User with id " + Id + " not found"));
        userMapper.updateUser(user, newUser);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String deleteUser(String Id){
        User user = userRepository.findById(Id).orElseThrow(() -> new UserNotFoundException("User with id " + Id + " not found"));
        userRepository.delete(user);
        return "Delete User Success!";
    }

    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponse(user);
    }
}
