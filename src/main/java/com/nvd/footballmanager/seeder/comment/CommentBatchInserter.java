package com.nvd.footballmanager.seeder.comment;

import com.nvd.footballmanager.model.entity.Comment;
import com.nvd.footballmanager.seeder.GenericBatchInserter;
import com.nvd.footballmanager.utils.Constants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

@Component
public class CommentBatchInserter implements GenericBatchInserter<Comment> {

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public CommentBatchInserter(JdbcTemplate jdbcTemplate,
                                RedisTemplate<String, Object> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void batchInsert(List<Comment> comments) {

        List<String> userIds = (List<String>) redisTemplate.opsForValue().get(Constants.USER_ID_CACHE);
        Random random = new Random();
        String sql = "INSERT INTO comments (id, content, feed_id, user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Comment comment = comments.get(i);
                ps.setString(1, comment.getId().toString());
                ps.setString(2, comment.getContent());
                ps.setString(3, comment.getFeed().getId().toString());
                String userId = userIds.get(random.nextInt(userIds.size()));
                ps.setString(4, userId);
                ps.setTimestamp(5, Timestamp.from(comment.getCreatedAt()));
                ps.setTimestamp(6, Timestamp.from(comment.getUpdatedAt()));
            }

            @Override
            public int getBatchSize() {
                return comments.size();
            }
        });
    }
}
