//package com.nvd.footballmanager.seeder.team;
//
//import com.nvd.footballmanager.model.entity.Team;
//import com.nvd.footballmanager.seeder.GenericBatchInserter;
//import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.List;
//
//@Component
//public class TeamBatchInserter implements GenericBatchInserter<Team> {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public TeamBatchInserter(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
////    public void batchInsert(List<Team> teams) {
////        String sql = "INSERT IGNORE INTO teams (id, name, address, rank_points, created_at, updated_at) VALUES "
////                + String.join(", ", Collections.nCopies(teams.size(), "(?, ?, ?, ?, ?, ?)"));
////
////        jdbcTemplate.update(connection -> {
////            PreparedStatement ps = connection.prepareStatement(sql);
////            int index = 1;
////            for (Team team : teams) {
////                ps.setString(index++, team.getId().toString());
////                ps.setString(index++, team.getName());
////                ps.setString(index++, team.getAddress());
////                ps.setInt(index++, team.getRankPoints());
////                ps.setTimestamp(index++, Timestamp.from(team.getCreatedAt()));
////                ps.setTimestamp(index++, Timestamp.from(team.getUpdatedAt()));
////            }
////            return ps;
////        });
////    }
//
//    public void batchInsert(List<Team> teams) {
//        String sql = "INSERT IGNORE INTO teams (id, name, logo, address, rank_points, created_at, updated_at)" +
//                " VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//            @Override
//            public void setValues(PreparedStatement ps, int i) throws SQLException {
//                Team team = teams.get(i);
//                ps.setString(1, team.getId().toString());
//                ps.setString(2, team.getName());
//                ps.setString(3, team.getLogo());
//                ps.setString(4, team.getAddress());
//                ps.setInt(5, team.getRankPoints());
//                ps.setTimestamp(6, Timestamp.from(team.getCreatedAt()));
//                ps.setTimestamp(7, Timestamp.from(team.getUpdatedAt()));
//            }
//
//            @Override
//            public int getBatchSize() {
//                return teams.size();
//            }
//        });
//
//    }
//}
//
