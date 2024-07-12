package com.nvd.footballmanager.service;

import com.nvd.footballmanager.dto.CommentDTO;
import com.nvd.footballmanager.dto.FeedDTO;
import com.nvd.footballmanager.dto.MatchRequestDTO;
import com.nvd.footballmanager.exceptions.AccessDeniedException;
import com.nvd.footballmanager.filters.CommentFilter;
import com.nvd.footballmanager.filters.FeedFilter;
import com.nvd.footballmanager.mappers.CommentMapper;
import com.nvd.footballmanager.mappers.FeedMapper;
import com.nvd.footballmanager.mappers.MatchRequestMapper;
import com.nvd.footballmanager.model.entity.Comment;
import com.nvd.footballmanager.model.entity.Feed;
import com.nvd.footballmanager.model.entity.MatchRequest;
import com.nvd.footballmanager.model.entity.User;
import com.nvd.footballmanager.repository.CommentRepository;
import com.nvd.footballmanager.repository.FeedRepository;
import com.nvd.footballmanager.repository.UserRepository;
import com.nvd.footballmanager.utils.Constants;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
//@RequiredArgsConstructor
public class FeedService extends BaseService<Feed, FeedDTO, FeedFilter, UUID> {
    private final FeedRepository feedRepository;
    private final FeedMapper feedMapper;
    private final MatchRequestService matchRequestService;
    private final MatchRequestMapper matchRequestMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public FeedService(FeedRepository feedRepository, FeedMapper feedMapper,
                       UserRepository userRepository,
                       MatchRequestMapper matchRequestMapper,
                       UserService userService,
                       CommentRepository commentRepository,
                       CommentMapper commentMapper,
                       MatchRequestService matchRequestService) {
        super(feedRepository, feedMapper);
        this.feedRepository = feedRepository;
        this.feedMapper = feedMapper;
        this.userRepository = userRepository;
        this.matchRequestService = matchRequestService;
        this.matchRequestMapper = matchRequestMapper;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }


    @Transactional
    @Override   // create a feed post (user can attach match request infor if has role manager of team)
    public FeedDTO create(FeedDTO feedDTO) {
        Feed feed = feedMapper.convertToEntity(feedDTO);
        User user = userRepository.findById(userService.getCurrentUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        feed.setUser(user);

        // handle the optional attach MatchRequest
        if (feedDTO.getMatchRequest() != null) {
            MatchRequestDTO matchRequestDTO = feedDTO.getMatchRequest();
            MatchRequest matchRequest = matchRequestMapper.convertToEntity(matchRequestService.create(matchRequestDTO));
            feed.setMatchRequest(matchRequest);
        }

        Feed savedFeed = feedRepository.save(feed);
        return feedMapper.convertToDTO(savedFeed);
    }

    @Override
    @Transactional
    public FeedDTO update(UUID uuid, FeedDTO feedDTO) {
        Feed feed = feedRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        UUID currentUserId = userService.getCurrentUser().getId();
        if (!feed.getUser().getId().equals(currentUserId)) {  // check if the user is not the owner of the feed
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }

        // handle the optional to update MatchRequest
        if (feedDTO.getMatchRequest() != null) {
            MatchRequestDTO matchRequestDTO = matchRequestService.update(feed.getMatchRequest().getId(), feedDTO.getMatchRequest());
            MatchRequest matchRequest = matchRequestMapper.convertToEntity(matchRequestDTO);
            feed.setMatchRequest(matchRequest);
        }

        Feed updated = feedMapper.updateEntity(feedDTO, feed);
//        updated.setId(id);
        feedRepository.save(updated);

        return feedMapper.convertToDTO(updated);
    }

    @Transactional
    public FeedDTO addComment(UUID id, String content) { // add comment to a feed post
        User user = userRepository.findById(userService.getCurrentUser().getId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
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

        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

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
        if (!comment.getUser().getId().equals(currentUserId)) { // check if the user is not the owner of the comment
            throw new AccessDeniedException(Constants.ACCESS_DENIED);
        }

        comment.setContent(content);

        commentRepository.save(comment);

        return feedMapper.convertToDTO(feed);
    }


    public Page<CommentDTO> findAllCommentsInFeed(CommentFilter filter) {
        feedRepository.findById(filter.getFeedId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));

        Page<Comment> comments = commentRepository.findAllWithFilter(filter.getPageable(), filter);
        return commentMapper.convertPageToDTO(comments);
    }

    public Page<FeedDTO> findAllByUser(FeedFilter filter) {
        userRepository.findById(filter.getUserId())
                .orElseThrow(() -> new EntityNotFoundException(Constants.ENTITY_NOT_FOUND));
        return super.findAll(filter);
    }
}


