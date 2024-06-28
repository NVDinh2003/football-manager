package com.nvd.footballmanager.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedDTO extends BaseDTO<UUID> {
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;

    private UUID userId;

    private List<CommentDTO> comments;

    private MatchRequestDTO matchRequest;
}
