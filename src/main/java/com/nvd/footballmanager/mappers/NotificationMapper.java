package com.nvd.footballmanager.mappers;

import com.nvd.footballmanager.dto.notification.NotificationDTO;
import com.nvd.footballmanager.model.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {BaseMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationMapper extends BaseMapper<Notification, NotificationDTO> {

    @Override
    @Mapping(target = "teamId", source = "team.id")
    NotificationDTO convertToDTO(Notification notification);

    @Override
    @Mapping(target = "team", ignore = true)
    Notification convertToEntity(NotificationDTO notificationDTO);

}