package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.CommentDTO;
import com.nvd.footballmanager.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {TeamMapper.class, BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper extends BaseMapper<Comment, CommentDTO> {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "username", source = "user.username")
    @Override
    CommentDTO convertToDTO(Comment entity);

    @Mapping(target = "user", ignore = true)
    @Override
    Comment convertToEntity(CommentDTO dto);
}
