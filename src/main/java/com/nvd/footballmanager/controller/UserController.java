package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController
        extends BaseController<User, UserDTO, UUID> {

    private final UserService userService;

    protected UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }


}
