package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.FeedDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.mappers.FeedMapper;
import com.nvd.footballmanager.mappers.MatchRequestMapper;
import com.nvd.footballmanager.model.entity.Comment;
import com.nvd.footballmanager.model.entity.Feed;
import com.nvd.footballmanager.model.entity.MatchRequest;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.repository.CommentRepository;
import com.nvd.footballmanager.repository.FeedRepository;
import com.nvd.footballmanager.repository.MatchRequestRepository;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class FeedService extends BaseService<Feed, FeedDTO, UUID> {
    private final FeedRepository feedRepository;
    private final FeedMapper feedMapper;
    private final MatchRequestRepository matchRequestRepository;
    private final MatchRequestMapper matchRequestMapper;
    private final UserRepository userRepository;
    private final UserService userService;

    private final CommentRepository commentRepository;

    public FeedService(FeedRepository feedRepository, FeedMapper feedMapper, UserRepository userRepository,
                       MatchRequestRepository matchRequestRepository, MatchRequestMapper matchRequestMapper,
                       UserService userService, CommentRepository commentRepository) {
        super(feedRepository, feedMapper);
        this.feedRepository = feedRepository;
        this.feedMapper = feedMapper;
        this.userRepository = userRepository;
        this.matchRequestRepository = matchRequestRepository;
        this.matchRequestMapper = matchRequestMapper;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }


    @Transactional
    @Override
    public FeedDTO create(FeedDTO feedDTO) {
        Feed feed = feedMapper.convertToEntity(feedDTO);
        User user = userRepository.findById(userService.getCurrentUser().getId()).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        feed.setUser(user);

        // Handle the optional MatchRequest
        if (feedDTO.getMatchRequest() != null) {
            MatchRequest matchRequest = matchRequestMapper.convertToEntity(feedDTO.getMatchRequest());
            matchRequestRepository.save(matchRequest);
            feed.setMatchRequest(matchRequest);
        }

        Feed savedFeed = feedRepository.save(feed);
        return feedMapper.convertToDTO(savedFeed);
    }

    @Transactional
    public FeedDTO addComment(UUID id, String content) {
        User user = userRepository.findById(userService.getCurrentUser().getId()).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        Feed feed = feedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(content)
                .feed(feed)
                .user(user)
                .build();

        commentRepository.save(comment);

        feed.getComments().add(comment);

        return feedMapper.convertToDTO(feedRepository.save(feed));
    }

    @Transactional
    public FeedDTO deleteComment(UUID id, UUID commentId) {

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        UUID currentUserId = userService.getCurrentUser().getId();
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }

        commentRepository.delete(comment);

        return feedMapper.convertToDTO(feed);
    }

    public FeedDTO updateComment(UUID id, UUID commentId, String content) {

        Feed feed = feedRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        UUID currentUserId = userService.getCurrentUser().getId();
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }

        comment.setContent(content);

        commentRepository.save(comment);

        return feedMapper.convertToDTO(feed);
    }
}


