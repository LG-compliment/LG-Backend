package com.ureca.compliment.user.mapper;


import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getId(), user.getName());
    }

    public static List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}