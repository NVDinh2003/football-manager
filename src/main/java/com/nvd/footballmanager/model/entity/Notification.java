package com.nvd.footballmanager.model.entity;

import com.nvd.footballmanager.model.BaseModel;
import com.nvd.footballmanager.model.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "notifications")
public class Notification extends BaseModel {

    private String message;
    @Enumerated(EnumType.STRING)
    @Column(length = 6, nullable = false)
    private NotificationStatus status = NotificationStatus.UNREAD;
}
