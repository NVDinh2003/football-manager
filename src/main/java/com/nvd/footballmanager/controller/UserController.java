package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.service.BaseService;

import java.util.UUID;


public class UserController extends BaseController<User, UserDTO, UUID> {
    protected UserController(BaseService<User, UserDTO, UUID> baseService) {
        super(baseService);
    }
}
