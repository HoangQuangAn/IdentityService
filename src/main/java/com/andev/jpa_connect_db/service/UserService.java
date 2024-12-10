package com.andev.jpa_connect_db.service;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    public UserResponse creatUser(UserCreationRequest reObject){

        if(userRepository.existsByUsername(reObject.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(reObject);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.ADMIN.name());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(reObject.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<User> getListUser(){
        return userRepository.findAll();
    }

    public UserResponse getById(String Id){
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
}
