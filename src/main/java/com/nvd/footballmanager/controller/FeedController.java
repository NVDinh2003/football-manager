package com.nvd.footballmanager.controller;

import com.nvd.footballmanager.dto.CustomApiResponse;
import com.nvd.footballmanager.dto.FeedDTO;
import com.nvd.footballmanager.filters.CommentFilter;
import com.nvd.footballmanager.filters.FeedFilter;
import com.nvd.footballmanager.model.entity.Feed;
import com.nvd.footballmanager.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/feeds")
public class FeedController extends BaseController<Feed, FeedDTO, FeedFilter, UUID> {

    private final FeedService feedService;


    protected FeedController(FeedService feedService) {
        super(feedService);
        this.feedService = feedService;
    }

    @GetMapping("/u/{userId}")
    public ResponseEntity<CustomApiResponse> findAllByUserId(
            @ModelAttribute FeedFilter filter,
            @PathVariable("userId") UUID userId) {
        filter.setUserId(userId);
        return ResponseEntity.ok(CustomApiResponse.success(feedService.findAllByUser(filter)));
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<CustomApiResponse> getAllCommentsInFeed(
            @ModelAttribute CommentFilter filter,
            @PathVariable("id") UUID id) {
        filter.setFeedId(id);
        return ResponseEntity.ok(CustomApiResponse.success(feedService.findAllCommentsInFeed(filter)));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CustomApiResponse> addComment(@PathVariable("id") UUID id, @RequestBody Map<String, String> body) {
        String content = body.get("content");
        return ResponseEntity.ok(CustomApiResponse.created(feedService.addComment(id, content)));
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CustomApiResponse> deleteComment(@PathVariable("id") UUID id,
                                                           @PathVariable("commentId") UUID commentId) {
        return ResponseEntity.ok(CustomApiResponse.success(feedService.deleteComment(id, commentId)));
    }

    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<CustomApiResponse> updateComment(@PathVariable("id") UUID id,
                                                           @PathVariable("commentId") UUID commentId,
                                                           @RequestBody Map<String, String> body) {
        String content = body.get("content");
        FeedDTO updatedFeed = feedService.updateComment(id, commentId, content);
        return ResponseEntity.ok(CustomApiResponse.success(updatedFeed));
    }

}
