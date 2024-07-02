package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.request.auth.UserRegistration;
import com.nvd.footballmanager.dto.user.UserDTO;
import com.nvd.footballmanager.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

// đặt trên 1 interface để tạo 1 lớp Mapper tương ứng,
@Mapper(componentModel = "spring", // tạo ra dưới dạng 1 bean trong spring
        // thuộc tính obj nguồn = null, thì thuộc tính obj đích = gt hiện tại or null
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserDTO> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    @Mapping(target = "password", constant = "password")
    User convertToEntity(UserDTO dto);

    @Override
    @Mapping(source = "password", target = "password", ignore = true)
    UserDTO convertToDTO(User entity);

    User convertForRegister(UserRegistration userRegistration);
}
