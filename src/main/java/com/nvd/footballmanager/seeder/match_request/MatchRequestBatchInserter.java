//package com.nvd.footballmanager.seeder.match_request;
//
//import com.nvd.footballmanager.model.entity.MatchRequest;
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
//public class MatchRequestBatchInserter implements GenericBatchInserter<MatchRequest> {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public MatchRequestBatchInserter(JdbcTemplate jdbcTemplate,
//                                     RedisTemplate<String, Object> redisTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Override
//    public void batchInsert(List<MatchRequest> matchRequests) {
//
//        List<String> teamIds = (List<String>) redisTemplate.opsForValue().get(Constants.TEAM_ID_CACHE);
//        Random random = new Random();
//
//        String sql = "INSERT IGNORE INTO match_requests (id, time, venue, location_details, match_type, note, status, team_id) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                MatchRequest request = matchRequests.get(i);
//                ps.setString(1, request.getId().toString());
//                ps.setTimestamp(2, Timestamp.valueOf(request.getTime()));
//                ps.setString(3, request.getVenue());
//                ps.setString(4, request.getLocationDetails());
//                ps.setString(5, request.getMatchType().name());
//                ps.setString(6, request.getNote());
//                ps.setString(7, request.getStatus().name());
//                ps.setString(8, teamIds.get(random.nextInt(teamIds.size())));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return matchRequests.size();
//            }
//        });
//    }
//}