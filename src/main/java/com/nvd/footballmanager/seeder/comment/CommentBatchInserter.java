//package com.nvd.footballmanager.seeder.comment;
//
//import com.nvd.footballmanager.model.entity.Comment;
//import com.nvd.footballmanager.model.entity.Feed;
//import com.nvd.footballmanager.repository.FeedRepository;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import com.nvd.footballmanager.utils.Constants;
//import jakarta.annotation.PostConstruct;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.Random;
//import java.util.stream.Collectors;
//
//@Component
//public class CommentBatchInserter implements GenericBatchInserter<Comment> {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final FeedRepository feedRepository;
//
//    public CommentBatchInserter(JdbcTemplate jdbcTemplate,
//                                RedisTemplate<String, Object> redisTemplate, FeedRepository feedRepository) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.redisTemplate = redisTemplate;
//        this.feedRepository = feedRepository;
//    }
//
//    private static final String FEED_ID_CACHE = "feed_id_cache";
//
//    @PostConstruct  // được gọi ngay sau khi các bean duoc khoi tao va các dependency da ược inject
//    public void init() {
//
//        if (Boolean.FALSE.equals(redisTemplate.hasKey(FEED_ID_CACHE))) {
//            List<Feed> feeds = feedRepository.findRandomLimit(110);
//
//            List<String> feed_ids = feeds.stream()
//                    .map(user -> user.getId().toString())
//                    .collect(Collectors.toList());
//            redisTemplate.opsForValue().set(FEED_ID_CACHE, feed_ids);
//        }
//
//    }
//
//    @Override
//    public void batchInsert(List<Comment> comments) {
//
//        List<String> userIds = (List<String>) redisTemplate.opsForValue().get(Constants.USER_ID_CACHE);
//        List<String> feedIds = (List<String>) redisTemplate.opsForValue().get(FEED_ID_CACHE);
//        Random random = new Random();
//        String sql = "INSERT INTO comments (id, content, feed_id, user_id, created_at, updated_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                Comment comment = comments.get(i);
//                ps.setString(1, comment.getId().toString());
//                ps.setString(2, comment.getContent());
//                ps.setString(3, feedIds.get(random.nextInt(feedIds.size())));
//                ps.setString(4, userIds.get(random.nextInt(userIds.size())));
//                ps.setTimestamp(5, Timestamp.from(comment.getCreatedAt()));
//                ps.setTimestamp(6, Timestamp.from(comment.getUpdatedAt()));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return comments.size();
//            }
//        });
//    }
//}
