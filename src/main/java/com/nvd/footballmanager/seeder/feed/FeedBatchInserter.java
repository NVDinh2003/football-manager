//package com.nvd.footballmanager.seeder.feed;
//
//import com.nvd.footballmanager.model.entity.Feed;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import com.nvd.footballmanager.utils.Constants;
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
//
//@Component
//public class FeedBatchInserter implements GenericBatchInserter<Feed> {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public FeedBatchInserter(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public void batchInsert(List<Feed> feeds) {
//        List<String> user_ids = (List<String>) redisTemplate.opsForValue().get(Constants.USER_ID_CACHE);
//        Random random = new Random();
//
//        String sql = "INSERT IGNORE INTO feeds (id, title, content, user_id, match_request_id, created_at, updated_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                Feed feed = feeds.get(i);
//                ps.setString(1, feed.getId().toString());
//                ps.setString(2, feed.getTitle());
//                ps.setString(3, feed.getContent());
//                ps.setString(4, user_ids.get(random.nextInt(user_ids.size())));
//
//                if (feed.getMatchRequest() != null) {
//                    ps.setString(5, feed.getMatchRequest().getId().toString());
//                } else {
//                    ps.setNull(5, java.sql.Types.VARCHAR); // Set null if MatchRequest is null
//                }
//
//                ps.setTimestamp(6, Timestamp.from(feed.getCreatedAt()));
//                ps.setTimestamp(7, Timestamp.from(feed.getUpdatedAt()));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return feeds.size();
//            }
//        });
//    }
//}
