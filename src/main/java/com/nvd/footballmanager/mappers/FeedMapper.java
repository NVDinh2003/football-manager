package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.CommentDTO;
import com.nvd.footballmanager.dto.FeedDTO;
import com.nvd.footballmanager.model.entity.Comment;
import com.nvd.footballmanager.model.entity.Feed;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {CommentMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FeedMapper extends BaseMapper<Feed, FeedDTO> {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(source = "comments", target = "comments", qualifiedByName = "convertToCommentDTO")
    @Override
    FeedDTO convertToDTO(Feed entity);

    @Named("convertToCommentDTO")
    default CommentDTO convertCommentToCommentDTO(Comment comment) {
        return CommentMapper.INSTANCE.convertToDTO(comment);
    }


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Override
    Feed convertToEntity(FeedDTO dto);
}
