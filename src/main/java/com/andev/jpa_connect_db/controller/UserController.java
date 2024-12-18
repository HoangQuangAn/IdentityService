package com.andev.jpa_connect_db.controller;


import com.andev.jpa_connect_db.dto.request.ApiResponse;
import com.andev.jpa_connect_db.dto.request.UserCreationRequest;
import com.andev.jpa_connect_db.dto.response.UserResponse;
import com.andev.jpa_connect_db.entity.User;
import com.andev.jpa_connect_db.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/create")
    ApiResponse<UserResponse> creatUser(@RequestBody @Valid UserCreationRequest userCreationRequest){
        ApiResponse<UserResponse> newApiResponse= new ApiResponse<>();
        newApiResponse.setResult(userService.creatUser(userCreationRequest));
        return newApiResponse;
    }

    @GetMapping("/get-all")
    ApiResponse<List<User>> getUserList(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        log.info("Username {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info( grantedAuthority.getAuthority()));
        return userService.getListUser();
    }

    @GetMapping("/{userId}")
    UserResponse getById(@PathVariable String userId){
        return userService.getById(userId);
    }

    @PutMapping("{userId}")
    UserResponse updateUser(@RequestBody @Valid UserCreationRequest userCreationRequest, @PathVariable String userId){
        return userService.updateUser(userCreationRequest, userId);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable String userId){
        return userService.deleteUser(userId);
    }
}
