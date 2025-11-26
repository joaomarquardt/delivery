package com.api.delivery.mappers;

import com.api.delivery.domain.User;
import com.api.delivery.dtos.requests.CreateUserRequest;
import com.api.delivery.dtos.requests.UpdateUserRequest;
import com.api.delivery.dtos.responses.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUserEntity(CreateUserRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponseList(List<User> user);

    @Mapping(target = "id", ignore = true)
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);
}
