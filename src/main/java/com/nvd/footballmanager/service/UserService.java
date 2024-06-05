package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.mappers.UserMapper;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.repository.UserRepository;

import java.util.UUID;

public class UserService extends BaseService<User, UserDTO, UUID> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    protected UserService(UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


}
