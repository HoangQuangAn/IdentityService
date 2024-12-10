package com.andev.jpa_connect_db.mapper;

import com.andev.jpa_connect_db.dto.request.UserCreationRequest;
import com.andev.jpa_connect_db.dto.response.UserResponse;
import com.andev.jpa_connect_db.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(@MappingTarget User user, UserCreationRequest userCreationRequest);
    UserResponse toUserResponse(User user);
}
