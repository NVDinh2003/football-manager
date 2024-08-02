package com.nvd.footballmanager.seeder.match;

import com.nvd.footballmanager.model.entity.Match;
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
public class MatchBatchInserter implements GenericBatchInserter<Match> {

    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    public MatchBatchInserter(JdbcTemplate jdbcTemplate, RedisTemplate<String, Object> redisTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void batchInsert(List<Match> matches) {
        List<String> teamIds = (List<String>) redisTemplate.opsForValue().get(Constants.TEAM_ID_CACHE);
        Random random = new Random();

        String sql = "INSERT IGNORE INTO matches (id, team1_scored, team2_scored, time, venue, confirmed, team1_id, team2_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Match match = matches.get(i);
                ps.setString(1, match.getId().toString());
                ps.setInt(2, match.getTeam1Scored());
                ps.setInt(3, match.getTeam2Scored());
                ps.setTimestamp(4, Timestamp.from(match.getTime())); // Convert Instant to Timestamp
                ps.setString(5, match.getVenue());
                ps.setBoolean(6, match.isConfirmed());
                String team1Id = teamIds.get(random.nextInt(teamIds.size()));
                String team2Id = teamIds.get(random.nextInt(teamIds.size()));
                while (team1Id.equals(team2Id))
                    team2Id = teamIds.get(random.nextInt(teamIds.size()));
                ps.setString(7, team1Id);
                ps.setString(8, team2Id);
                ps.setTimestamp(9, Timestamp.from(match.getCreatedAt()));
                ps.setTimestamp(10, Timestamp.from(match.getUpdatedAt()));
            }

            @Override
            public int getBatchSize() {
                return matches.size();
            }
        });
    }
}

